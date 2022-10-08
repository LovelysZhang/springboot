```
GET _search
{
  "query": {
    "match_all": {}
  }
}
# ----split----
PUT /product/_doc/01
{
  "name": "xiaomi nfc phone",
  "desc": "支持nfc，高性能手机",
  "price": 4999,
  "tag": [
    "xingjiabi",
    "fashou",
    "123"
  ]
}
DELETE /product/_doc/01

# ----split----
GET /product/_doc/01


#查看mapping
GET /product/_mapping

#手动映射类型
PUT /product
{
  "mappings": {
    "properties": {
      "field": {
        "mapping_parameter": "parameter_value"
      }
    }
  }
}
# ----split----
# ----搜索和查询----
PUT /product/_doc/1
{
  "name": "xiaomi phone",
  "desc": "shouji zhong de zhandouji",
  "date": "2021-06-01",
  "price": 3999,
  "tags": [
    "xingjiabi",
    "fashao",
    "buka"
  ]
}
PUT /product/_doc/2
{
  "name": "xiaomi nfc phone",
  "desc": "zhichi quangongneng nfc,shouji zhong de jianjiji",
  "date": "2021-06-02",
  "price": 4999,
  "tags": [
    "xingjiabi",
    "fashao",
    "gongjiaoka"
  ]
}
PUT /product/_doc/3
{
  "name": "nfc phone",
  "desc": "shouji zhong de hongzhaji",
  "date": "2021-06-03",
  "price": 2999,
  "tags": [
    "xingjiabi",
    "fashao",
    "menjinka"
  ]
}
PUT /product/_doc/4
{
  "name": "xiaomi erji",
  "desc": "erji zhong de huangmenji",
  "date": "2021-04-15",
  "price": 999,
  "tags": [
    "low",
    "bufangshui",
    "yinzhicha"
  ]
}
PUT /product/_doc/5
{
  "name": "hongmi erji",
  "desc": "erji zhong de kendeji 2021-06-01",
  "date": "2021-04-16",
  "price": 399,
  "tags": [
    "lowbee",
    "xuhangduan",
    "zhiliangx"
  ]
}
# 查询所有
GET /product/_search
GET /product/_search
{
  "_source": false,
  "query": {
    "match_all": {}
  }
}
#带参数
GET /product/_search?q=name:xiaomi
#分页
GET /product/_search?from=0&size=2&sort=price:asc
#精准匹配 exact value
GET /product/_search?q=date:2021-06-01
#_all搜索 相当于在所有有索引的字段中检索
GET /product/_search?q=2021-06-01

PUT /product2/_doc/1
{
  "name": "xiaomi erji",
  "desc": "erji zhong de huangmenji",
  "date": "2021-04-15",
  "price": 999,
  "tags": [
    "low",
    "bufangshui",
    "yinzhicha"
  ]
}

# ----split----
# ----DSL----
#验证索引会被分词
GET _analyze
{
  "analyzer": "standard",
  "text": "xiaomi nfc phone"
}

GET product/_search
{
  "query": {
    "match": {
      "name": "phone xiaomi"
    }
  }
}

GET product/_search
{
  "query": {
    "match_phrase": {
      "name": "xiaomi phone"
    }
  }
}

GET product/_search
{
  "query": {
    "term": {
      "name": "xiaomi phone"
    }
  }
}

# ----split----
# ----过滤器、布尔查询----

#filter不计算相关度评分，所以查询结果_score都是"boost": 1.2 
GET product/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "name": "phone"
        }
      },
      "boost": 1.2 
    }
  }
}
#组合查询
GET product/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "xiaomi phone"
          }
        },
        {
          "match": {
            "desc": "shouji zhong"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "price": {
              "lte": 1000
            }
          }
        }
      ]
    }
  }
}





# ----split----
# ----分词器----
#normalization
GET _analyze
{
"text": "Mr. Ma is an excellent teacher",
"analyzer": "english"
}

#character filter
##HTML Strip Character Filter
###测试数据<p>I&apos;m so <a>happy</a>!</p>
DELETE my_index

PUT my_index
{
  "settings": {
    "analysis": {
      "char_filter": {
        "my_char_filter": {
          "type": "html_strip",
          "escaped_tags": [
            "a"
          ]
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "keyword",
          "char_filter": [
            "my_char_filter"
          ]
        }
      }
    }
  }
}


GET my_index/_analyze
{
  "analyzer": "my_analyzer",
  "text": ["<p>I&apos;m so <a>happy</a>!</p>"]
}


##Mapping Character Filter
DELETE my_index
PUT my_index
{
  "settings": {
    "analysis": {
      "char_filter": {
        "my_char_filter": {
          "type": "mapping",
          "mappings": [
            "滚 => *",
            "垃 => *",
            "圾 => *"
          ]
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "keyword",
          "char_filter": [
            "my_char_filter"
          ]
        }
      }
    }
  }
}

GET my_index/_analyze
{
  "analyzer": "my_analyzer",
  "text": ["滚你大爷"]
}


##Pattern Replace Character Filter
DELETE my_index
PUT my_index
{
  "settings": {
    "analysis": {
      "char_filter": {
        "my_char_filter": {
          "type": "pattern_replace",
          "pattern": """(\d{3})\d{4}(\d{4})""",
          "replacement": "$1****$2"
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "keyword",
          "char_filter": [
            "my_char_filter"
          ]
        }
      }
    }
  }
}

GET my_index/_analyze
{
  "analyzer": "my_analyzer",
  "text": ["手机号18629002900"]
}



#***********************split*************************
#token filter
DELETE test_index
PUT /test_index
{
  "settings": {
    "analysis": {
      "filter": {
        "my_synonym": {
          "type": "synonym_graph",
          "synonyms_path": "analysis/synonym.txt"
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "ik_max_word",
          "filter": [
            "my_synonym"
          ]
        }
      }
    }
  }
}
GET test_index/_analyze
{
  "analyzer": "my_analyzer",
  "text": [
    "蒙丢丢，大G，霸道，daG"
  ]
}

#***********************split*************************
#tokenizer分词器
PUT /test_index
DELETE test_index
#大小写
GET test_index/_analyze
{
  "tokenizer": "standard",
  "filter": [
    "lowercase"
  ],
  "text": [
    "AASD ASDA SDASD ASDASD"
  ]
}
#中文分词器
GET test_index/_analyze
{
  "tokenizer": "ik_max_word",
  "text": ["我爱北京天安门","天安门上太阳升"]
}
#自定义分词器
DELETE custom_analysis
PUT custom_analysis
{
  "settings": {
    "analysis": {
      "char_filter": {
        "my_char_filter": {
          "type": "mapping",
          "mappings": [
            "&=>and",
            "|=>or",
            "!=>not"
          ]
        },
        "html_strip_char_filter": {
          "type": "html_strip",
          "escaped_tags": [
            "a"
          ]
        }
      },
      "filter": {
        "my_stopword": {
          "type": "stop",
          "stopwords": [
            "is",
            "in",
            "the",
            "a",
            "at",
            "for"
          ]
        }
      },
      "tokenizer": {
        "my_tokenizer": {
          "type": "pattern",
          "pattern": """[\s,.!?]"""
        }
      },
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "char_filter": [
            "my_char_filter",
            "html_strip_char_filter"
          ],
          "filter": [
            "my_stopword",
            "lowercase"
          ],
          "tokenizer": "my_tokenizer"
        }
      }
    }
  }
}

GET custom_analysis/_analyze
{
  "analyzer": "my_analyzer",
  "text": [
    "What is ,<a>as.df</a> ss<p> in ? &</p> | is ! in the a at for "
  ]
}


#-----------split-----------------------

#聚合查询
PUT product
{
  "mappings" : {
      "properties" : {
        "createtime" : {
          "type" : "date"
        },
        "date" : {
          "type" : "date"
        },
        "desc" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          },
          "analyzer":"ik_max_word"
        },
        "lv" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "name" : {
          "type" : "text",
          "analyzer":"ik_max_word",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "price" : {
          "type" : "long"
        },
        "tags" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "type" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    }
}
## 初始化数据
PUT /product/_doc/1
{
    "name" : "小米手机",
    "desc" :  "手机中的战斗机",
    "price" :  3999,
    "lv":"旗舰机",
    "type":"手机",
    "createtime":"2020-10-01T08:00:00Z",
    "tags": [ "性价比", "发烧", "不卡顿" ]
}
PUT /product/_doc/2
{
    "name" : "小米NFC手机",
    "desc" :  "支持全功能NFC，手机中的滑翔机",
    "price" :  4999,
        "lv":"旗舰机",
    "type":"手机",
    "createtime":"2020-05-21T08:00:00Z",
    "tags": [ "性价比", "发烧", "公交卡" ]
}
PUT /product/_doc/3
{
    "name" : "NFC手机",
    "desc" :  "手机中的轰炸机",
    "price" :  2999,
        "lv":"高端机",
    "type":"手机",
    "createtime":"2020-06-20",
    "tags": [ "性价比", "快充", "门禁卡" ]
}
PUT /product/_doc/4
{
    "name" : "小米耳机",
    "desc" :  "耳机中的黄焖鸡",
    "price" :  999,
        "lv":"百元机",
    "type":"耳机",
    "createtime":"2020-06-23",
    "tags": [ "降噪", "防水", "蓝牙" ]
}
PUT /product/_doc/5
{
    "name" : "红米耳机",
    "desc" :  "耳机中的肯德基",
    "price" :  399,
    "type":"耳机",
        "lv":"百元机",
    "createtime":"2020-07-20",
    "tags": [ "防火", "低音炮", "听声辨位" ]
}
PUT /product/_doc/6
{
    "name" : "小米手机10",
    "desc" :  "充电贼快掉电更快，超级无敌望远镜，高刷电竞屏",
    "price" :  "",
        "lv":"旗舰机",
    "type":"手机",
    "createtime":"2020-07-27",
    "tags": [ "120HZ刷新率", "120W快充", "120倍变焦" ]
}
PUT /product/_doc/7
{
    "name" : "挨炮 SE2",
    "desc" :  "除了CPU，一无是处",
    "price" :  "3299",
        "lv":"旗舰机",
    "type":"手机",
    "createtime":"2020-07-21",
    "tags": [ "割韭菜", "割韭菜", "割新韭菜" ]
}
PUT /product/_doc/8
{
    "name" : "XS Max",
    "desc" :  "听说要出新款12手机了，终于可以换掉手中的4S了",
    "price" :  4399,
        "lv":"旗舰机",
    "type":"手机",
    "createtime":"2020-08-19",
    "tags": [ "5V1A", "4G全网通", "大" ]
}
PUT /product/_doc/9
{
    "name" : "小米电视",
    "desc" :  "70寸性价比只选，不要一万八，要不要八千八，只要两千九百九十八",
    "price" :  2998,
        "lv":"高端机",
    "type":"耳机",
    "createtime":"2020-08-16",
    "tags": [ "巨馍", "家庭影院", "游戏" ]
}
PUT /product/_doc/10
{
    "name" : "红米电视",
    "desc" :  "我比上边那个更划算，我也2998，我也70寸，但是我更好看",
    "price" :  2999,
    "type":"电视",
        "lv":"高端机",
    "createtime":"2020-08-28",
    "tags": [ "大片", "蓝光8K", "超薄" ]
}
PUT /product/_doc/11
{
  "name": "红米电视",
  "desc": "我比上边那个更划算，我也2998，我也70寸，但是我更好看",
  "price": 2998,
  "type": "电视",
  "lv": "高端机",
  "createtime": "2020-08-28",
  "tags": [
    "大片",
    "蓝光8K",
    "超薄"
  ]
}




##聚合 统计不同标签的商品数量
GET product/_search
{
  "aggs": {
    "aggs_tag": {
      "terms": {
        "field": "tags.keyword",
        "size": 3
      }
    }
  }
}

#修改fielddata
POST product/_mapping
{
  "properties":{
    "tags":{
      "type":"text",
      "fielddata":true
    }
  }
}
GET product/_search
{
  "aggs": {
    "aggs_tag": {
      "terms": {
        "field": "tag",
        "size": 3
      }
    }
  }
}

GET product/_mapping

##指标聚合

###最贵、最便宜和avg三个指标
GET product/_search
{
 "size": 20 ,
 "aggs": {
   "max_price": {
     "max": {
       "field": "price"
     }
   },
   "min_price":{
     "min": {
       "field": "price"
     }
   },
    "avg_price":{
     "avg": {
       "field": "price"
     }
   }
 }
}
####所有指标
GET product/_search
{
  "size": 20,
  "aggs": {
    "price": {
      "stats": {
        "field": "price"
      }
    }
  }
}
GET product/_search
{
  "size": 20,
  "aggs": {
    "name_count": {
      "cardinality": {
        "field": "name.keyword"
      }
    }
  }
}

##管道聚合 二次聚合
####统计平均价格最低的商品分类
GET product/_search
{
  "size": 20,
  "aggs": {
    "type_bucket": {
      "terms": {
        "field": "type.keyword",
        "size": 10
      },
      "aggs": {
        "price_bucket": {
          "avg": {
            "field": "price"
          }
        }
      }
    },
    "min_bucket":{
      "min_bucket": {
        "buckets_path": "type_bucket>price_bucket"
      }
    }
  }
}


##复杂嵌套查询
####按照不同类型商品的不同级别的数量
GET product/_search
{
  "size": 20,
  "aggs": {
    "type_lv": {
      "terms": {
        "field": "type.keyword"
      },
      "aggs": {
        "lv": {
          "terms": {
            "field": "lv.keyword"
          }
        }
      }
    }
    
  }
}

#### 按照lv分桶 输出每个桶的具体价格信息
GET product/_search
{
  "aggs": {
    "lv_price": {
      "terms": {
        "field": "lv.keyword"
      },
      "aggs": {
        "price": {
          "stats": {
            "field": "price"
          }
        }
      }
    }
  }
}

#### 统计不同类型不同档次的商品价格信息
GET product/_search
{
  "aggs": {
    "type_lv": {
      "terms": {
        "field": "type.keyword"
      },
      "aggs": {
        "lv": {
          "terms": {
            "field": "lv.keyword"
          },
          "aggs": {
            "price_status": {
              "stats": {
                "field": "price"
              }
            },
            "tags_buckets": {
              "terms": {
                "field": "tags.keyword"
              }
            }
          }
        }
      }
    }
  }
}



##基于查询结果聚合（上面是基于聚合结果聚合）
GET product/_search
{
  "size": 20,
  "query": {
    "range": {
      "price": {
        "gte": 1000
      }
    }
  },
  "aggs": {
    "tags_buckets": {
      "terms": {
        "field": "tags.keyword",
        "size": 100
      }
    }
  }
}
##基于filter结果聚合
GET product/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "gte": 1000
          }
        }
      }
    }
  },
  "aggs": {
    "tags_buckets": {
      "terms": {
        "field": "tags.keyword",
        "size": 100
      }
    }
  }
}


## 基于聚合的查询，感觉不好 先聚合是所有的数据聚合
GET product/_search
{
  "aggs": {
    "tags_buckets": {
      "terms": {
        "field": "tags.keyword",
        "size": 100
      }
    }
  },
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "gte": 1000
          }
        }
      }
    }
  }
}
##聚合排序 _count数量 _key _term字典顺序

GET product/_search?size=0
{
  "aggs": {
    "tags_aggs": {
      "terms": {
        "field": "tags.keyword",
        "size": 100,
        "order": {
          "_key": "desc"
        }
      },
      "aggs": {
        "second_sort": {
          "terms": {
            "field": "lv.keyword",
            "order": {
              "_count": "asc"
            }
          }
        }
      }
    }
  }
}

#------------------split--------------------
GET product/_doc/2
#脚本CRUD
POST product/_update/2
{
  "script": "ctx._source.price-=1"
}
###reindex备份，很耗时
POST _reindex
{
  "source": {
    "index": "product"
  },
  "dest": {
    "index": "product2"
  }
}

###小米10出了新款 新增了tag叫做“无线充电”
POST product/_update/6
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.tags.add('无线充电')"
  }
}
GET product/_doc/6

#upsert === update+insert

POST product/_update/15
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.price+=100"
  },
  "upsert": {
    "name": "小米手机10",
    "desc": "充电贼快掉电更快，超级无敌望远镜，高刷电竞屏",
    "price": 1999
  }
}
GET product/_doc/15
###脚本查询
GET product/_search
{
  "script_fields": {
    "my_price": {
      "script": {
        "lang": "expression",
        "source": "doc['price']"
      }
    }
  }
}
###参数化脚本


###脚本模板 实现脚本复用


###scripting 函数式编程
POST product/_update/1
{
  "script": {
    "lang": "painless",
    "source": """
    ctx._source.tags.add(params.tag_name);
    ctx._source.price+=100;
    """,
    "params": {
      "tag_name": "无线秒冲"
    }
  }
}
GET product/_doc/1

###正则匹配  name中包含【小米】

POST product/_update/1
{
  "script": {
    "lang": "painless",
    "source": """
    if(ctx._source.name ==~/[\s\S]*小米[\s\S]*/){
      ctx._source.name+="****"
    }else{
      ctx.op="noop"
    }
    """
  }
}


####深度堆栈不安全，低版本可能需要开启 scripting.painless.reges.enable:true




##doc['feild'].value和param['_source']['feild']区别

#统计男性嫌疑人的数量

#批量查询
GET product/_mget
{
  "docs": [
    {
      "_id": 2
    }
  ]
}

GET product/_mget
{
  "ids": [
    2,
    3
  ]
}
####指定查询想要的字段
GET product/_mget
{
  "docs": [
    {
      "_id": 2,
      "_source": [
        "name",
        "price"
      ]
    },
    {
      "_id": 3
    }
  ]
}


#索引的操作类型 op_type

GET test_index/_doc/1
##create 
PUT test_index/_doc/1
{
  "test_feild": "test",
  "test_title": "title"
}

PUT test_index/_doc/2/_create
{
  "test_feild": "test",
  "test_title": "title"
}
PUT test_index/_create/3
{
  "test_feild": "test",
  "test_title": "title"
}
##delete 懒删除
DELETE test_index/_doc/3

##update
####全量替换
GET test_index/_search

PUT test_index/_doc/aaa
{
  "test_feild": "test****",
  "test_title": "title****"
}

####部分更新
POST test_index/_update/aaa
{
  "doc": {
    "test_title": "title####"
  }
}

##index
#### 可以全量替换 或者创建
PUT test_index/_doc/4?op_type=index
{
  "test_feild": "test****",
  "test_title": "title****",
  "test_name": "name****"
}



#_bulk批量操作
GET test_index/_search

POST test_index/_bulk
{"create":{"_id":5}}
{"test_title":"_bulk create"}


POST test_index/_bulk
{"delete":{"_id":5}}


POST test_index/_bulk
{"update":{"_id":2}}
{"doc":{"test_title":"_bulk create2"}}

##批量操作
POST test_index/_bulk
{"update":{"_id":2}}
{"doc":{"test_title":"_bulk create3"}}
{"create":{"_id":5}}
{"test_title":"_bulk create"}


#前缀匹配 prefix


#通配符 wildcard




#正则 regexp
GET product/_search
{
  "query": {
    "regexp": {
      "desc": {
        "value": ""
      }
    }
  }
}

#fuzzy 模糊查询
GET product/_search

GET product/_search
{
  "query": {
    "fuzzy": {
      "name": "小手机"
    }
  }
}

GET product/_search
{
  "query": {
    "match": {
      "name": {
        "query": "小手机",
        "fuzziness": 1
      }
    }
  }
}

#match_phrase 短语前缀




#ngram 和 edge-ngram
##tokenizer 分词器
GET _analyze
{
  "tokenizer": "ngram",
  "text": [
    "reba always"
  ]
}

##tokenfilter  词项过滤器
GET _analyze
{
  "tokenizer": "standard",
  "filter": ["ngram"], 
  "text": [
    "reba always"
  ]
}




#term suggest
DELETE news

POST _bulk
{ "index" : { "_index" : "news","_id":1 } }
{ "title": "baoqiang bought a new hat with the same color of this font, which is very beautiful baoqiangba baoqiangda baoqiangdada baoqian baoqia"}
{ "index" : { "_index" : "news","_id":2 } }
{ "title": "baoqiangge gave birth to two children, one is upstairs, one is downstairs baoqiangba baoqiangda baoqiangdada baoqian baoqia"}
{ "index" : { "_index" : "news","_id":3} }
{ "title": "baoqiangge 's money was rolled away baoqiangba baoqiangda baoqiangdada baoqian baoqia"}
{ "index" : { "_index" : "news","_id":4} }
{ "title": "baoqiangda baoqiangda baoqiangda baoqiangda baoqiangda baoqian baoqia"}

































































```