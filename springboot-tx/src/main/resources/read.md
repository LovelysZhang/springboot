# 操作数据看
java操作数据库的流程
每次操作db都需要加载数据库驱动
获取连接
获取PreparedStatement
执行sql
关闭PreparedStatement
关闭连接



# 事务源码分析


## 1、定义事务的属性信息(TransactionDefinition)

相关信息

事务传播行为、隔离级别、超时时间、是否是只读事务、事务名称，


DefaultTransactionDefinition：TransactionDefinition接口的默认的一个实现，编程式事务中通常可以使用这个

RuleBasedTransactionAttribute：声明式事务中用到的是这个，这个里面对于事务回滚有一些动态匹配的规则

## 2、定义事务管理器(PlatformTransactionManager)

负责：获取事务、提交事务、回滚事务，Spring中用PlatformTransactionManager接口表示事务管理器


PlatformTransactionManager有多个实现类，用来应对不同的环境，比如你操作db用的是hibernate或者mybatis，那么用到的事务管理器是不一样的，常见的事务管理器实现有下面几个

- JpaTransactionManager：如果你用jpa来操作db，那么需要用这个管理器来帮你控制事务。

- DataSourceTransactionManager：如果你用是指定数据源的方式，比如操作数据库用的是：JdbcTemplate、mybatis、ibatis，那么需要用这个管理器来帮你控制事务。

- HibernateTransactionManager：如果你用hibernate来操作db，那么需要用这个管理器来帮你控制事务。

- JtaTransactionManager：如果你用的是java中的jta来操作db，这种通常是分布式事务，此时需要用这种管理器来控制事务。

## 3、获取事务
以REQUIRED中嵌套一个REQUIRED_NEW来进行说明，也就是事务中嵌套一个新的事务。

```
TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
```
### 3.1 getTransaction

doGetTransaction()
    TransactionSynchronizationManager.getResource():从resource ThreadLocal中查找transactionManager.datasource绑定的ConnectionHolder对象
        doGetResource

// 默认传播行为，调用suspend
SuspendedResourcesHolder suspendedResources = suspend()//挂起

startTransaction()//开始事务
    DefaultTransactionStatus status = newTransactionStatus()//创建事务状态对象
    doBegin(transaction, definition)//开启事务
    prepareSynchronization(status, definition)//准备事务同步


#### 3.1-1 doBegin
获取连接
```java
if (!txObject.hasConnectionHolder() ||
        txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
        Connection newCon = obtainDataSource().getConnection();
        if (logger.isDebugEnabled()) {
        logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
        }
        txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
        }
```
// 启动事务同步
txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
// 事务隔离级别
Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
txObject.setPreviousIsolationLevel(previousIsolationLevel);
// 是否只读
txObject.setReadOnly(definition.isReadOnly());
//自动提交。可以在配置文件制定是否自动提交。手动提交对某些jdbc驱动影响性能
```java
if (con.getAutoCommit()) {
	txObject.setMustRestoreAutoCommit(true);
	con.setAutoCommit(false);
}
```
// 准备事务连接
prepareTransactionalConnection(con, definition);
// 事务自动开启
txObject.getConnectionHolder().setTransactionActive(true);
// 事务超时设备
int timeout = determineTimeout(definition);
txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
// 连接connection绑定到当前线程
```java
if (txObject.isNewConnectionHolder()) {
	TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());
}
```
    bindResource()// 绑定resource。放到threadlocalMap

#### 3.1-2 prepareSynchronization 准备事务同步
作用是，开启一个新事务的时候，会将事务的状态、隔离级别、是否是只读事务、事务名称丢到TransactionSynchronizationManager中的各种对应的ThreadLocal中，
方便在当前线程中共享这些数据。

```java
protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
    // 是否是一个新事务
	if (status.isNewSynchronization()) {
		TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
		TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
				definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT ?
						definition.getIsolationLevel() : null);
		TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
		TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
        // 初始化事务同步
        // threadlocal 中放一个 LinkedHashSet
		TransactionSynchronizationManager.initSynchronization();
	}
}
```


### 3.2、小结
- 1、获取db连接：从事务管理器的datasource中调用getConnection获取一个新的数据库连接，将连接置为手动提交
- 2、将datasource关联连接丢到ThreadLocal中：将第一步中获取到的连丢到ConnectionHolder中，然后将事务管理器的datasource->ConnectionHolder丢到了resource ThreadLocal中，这样我们可以通过datasource在ThreadLocal中获取到关联的数据库连接
- 3、准备事务同步：将事务的一些信息放到ThreadLocal中

## 4、事务方法执行CRUD
jdbcTemplate.update() 最终执行到jdbctemplate#execute方法里
```java
org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementCallback<T>){
        //获取数据库连接
        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        //通过conn执行db操作
        ....
        }
```

DataSourceUtils.getConnection(obtainDataSource());
```java
// 获取同一个DataSource的连接
ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
if (conHolder != null && (conHolder.hasConnection() || conHolder.isSynchronizedWithTransaction())) {
    conHolder.requested();
    if (!conHolder.hasConnection()) {logger.debug("Fetching resumed JDBC Connection from DataSource");conHolder.setConnection(fetchConnection(dataSource));
    }
    return conHolder.getConnection();
}
```

**可以得出一个结论：如果要让最终执行的sql受spring事务控制，那么事务管理器中datasource对象必须和jdbctemplate.datasource是同一个。**


## 5、提交事务commit
执行完提交事务

```java
// org.springframework.transaction.support.AbstractPlatformTransactionManager#commit
class AbstractPlatformTransactionManager{
    public final void commit(TransactionStatus status) throws TransactionException {
        // 事务是否已经完成，此时还未完成，如果事务完成了，再来调用commit方法会报错
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException(
                    "Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        // 事务状态
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        // 是否需要回滚。rollbackOnly属性
        if (defStatus.isLocalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Transactional code has requested rollback");
            }
            processRollback(defStatus, false);
            return;
        }
        // 是否需要回滚
        if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
            }
            processRollback(defStatus, true);
            return;
        }
        // 提交事务
        processCommit(defStatus);
    }
}
```
提交事务 processCommit(defStatus);
    --->cleanupAfterCompletion
        --->doCleanupAfterCompletion //清理工作主要做的事情就是释放当前线程占有的一切资源，然后将被挂起的事务恢复。
回滚 processRollback() // 和提交事务类似
    --->doRollback()
        --->cleanupAfterCompletion()


## 5、存在事务的情况
另外一个流程，REQUIRED中嵌套一个REQUIRED_NEW，然后走到REQUIRED_NEW的时候，代码是如何运行的？

大致的过程如下

#### 1、判断上线文中是否有事务
isExistingTransaction
    --->handleExistingTransaction // 存在事务情况下会执行


#### 2、挂起当前事务 suspend

主要目的：将当前事务中的一切信息保存到SuspendedResourcesHolder对象中，相当于事务的快照，后面恢复的时候用；
然后将事务现场清理干净，主要是将一堆存储在ThreadLocal中的事务数据干掉。

--->doSuspend //将datasource->connectionHolder从resource ThreadLocal中解绑，然后将connectionHolder返回

#### 3、开启新事务，并执行新事务

#### 4、恢复被挂起的事务 resume
通过SuspendedResourcesHolder对象中，将被挂起的事务恢复，SuspendedResourcesHolder对象中保存了被挂起的事务所有信息


## 6 事务扩展回调接口 TransactionSynchronization
spring事务运行的过程中，给开发者预留了一些扩展点，在事务执行的不同阶段，将回调扩展点中的一些方法。



