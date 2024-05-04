# Elasticsearch Coerce: What Is It and How to Use It

## Introduction

In Elasticsearch, the `coerce` parameter is used to convert a field to a different data type if needed (and if possible). This can be useful when you want to be more tolerant to the incoming data. In this article, we will discuss what the `coerce` parameter is and how to use it in Elasticsearch.

## The Coerce Parameter

The `coerce` parameter is set on a field in the mapping definition. It is used to automatically convert a field to a different data type if needed. For example, if you have a field that is defined as a `long` but you receive a value that is a `string`, Elasticsearch will automatically try to convert the value to a `long` if the `coerce` parameter is set to `true`.

By default, the `coerce` parameter is set to `false`, so no automatic conversion will be done. If you want to enable automatic conversion, you need to set the `coerce` parameter to `true` explicitly.

The current default settings can be checked as follows:

```bash
# index name is mc-test-index
curl http://localhost:9200/mc-test-index/_settings\?include_defaults=true | jq '.[].defaults.index.mapping.coerce'
```

## Coerce Parameter in action

Let's see an example of how the `coerce` parameter works. We will create an index with a field that is defined as a `long` but we will try to index a `float` value. We will set the `coerce` parameter to `true` so that Elasticsearch will automatically convert the value to a `long`.

First, let's create an index with the following mapping:

```edql
# creating an index with mapping
PUT /mc-test-index
PUT /mc-test-index/_mapping
{
  "properties": {
    "long_field": {
      "type": "long",
      "coerce": false
    },
    "long_field_coerced": {
      "type": "long",
      "coerce": true
    }
  }
}
```

So, we have one field without coercion and one field with the `coerce` parameter set to `true`. Let's see what's different when we try to index a `float` value.

```edql
POST /mc-test-index/_doc/1
{
  "long_field": 1.1,
  "long_field_coerced": 1.1
}
```

If you run the above command, you will see that the indexing fails with the following error:

```json
{
  "error": {
    "root_cause": [
      {
        "type": "document_parsing_exception",
        "reason": "[2:17] failed to parse field [long_field] of type [long] in document with id '1'. Preview of field's value: '1.1'"
      }
    ],
    "type": "document_parsing_exception",
    "reason": "[2:17] failed to parse field [long_field] of type [long] in document with id '1'. Preview of field's value: '1.1'",
    "caused_by": {
      "type": "illegal_argument_exception",
      "reason": "1.1 cannot be converted to Long without data loss"
    }
  },
  "status": 400
}
```

So let's try again, but this time we'll only put the float into the `long_field_coerced` field:

```edql
POST /mc-test-index/_doc/11
{
  "long_field": 1,
  "long_field_coerced": 1.1
}
```

Now, this works, as it should. The `long_field_coerced` field is automatically converted to a `long` value. Let's create another record, this time with the long values in both fields:

```edql
PUT /mc-test-index/_create/1
{
  "long_field": 1,
  "long_field_coerced": 1
}
```

Now we have some data to run queries on. Let's see what happens when we get this data:

```edql
GET /mc-test-index/_search?filter_path=hits.hits._source
```

You should see the following output:

```json
{
  "hits": {
    "hits": [
      {
        "_source": {
          "long_field": 1,
          "long_field_coerced": 1.1
        }
      },
      {
        "_source": {
          "long_field": 1,
          "long_field_coerced": 1
        }
      }
    ]
  }
}
```

OK, we say. So the `long_field_coerced` is actually still a float? But is it, really? Let's try to search for the `long_field_coerced` field with a float value:

```edql 
POST /mc-test-index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "long_field_coerced": 1.1
          }
        }
      ]
    }
  }
}
```

Or with this range query. Let's see if we can get that record with 1.1 in the `long_field_coerced` field this way.

```edql
POST /mc-test-index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "long_field_coerced": {
              "gt": 1
            }
          }
        }
      ]
    }
  }
}

```

Surprise, surprise! Both searches return no hits. But how come? We see the 1.1 in that document, right? Well, yes and no. The `long_field_coerced` field is actually a `long` field in the index, but it is displayed as a float in the `_source` field in the search results, because that's how it's been posted when indexed. The `_source` field [contains the original JSON body of the document](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-source-field.html), and it is not the same as the field in the index. The field in the index is a `long` field, and it will be treated as such when queried.

Right, then. So how do we see the actual value of the `long_field_coerced` field in the index? We can use the `fields` parameter in the search query to get the actual value of the field in the index.

```edql
POST /mc-test-index/_search?filter_path=hits.hits&_source=false
{
  "query": {
    "match_all": {}
  },
  "fields": ["long_field", "long_field_coerced"]
}
```

The result we're getting is looking as follows.

```json
{
  "hits": {
    "hits": [
      {
        "_index": "mc-test-index",
        "_id": "11",
        "_score": 1,
        "fields": {
          "long_field": [
            1
          ],
          "long_field_coerced": [
            1
          ]
        }
      },
      {
        "_index": "mc-test-index",
        "_id": "1",
        "_score": 1,
        "fields": {
          "long_field": [
            1
          ],
          "long_field_coerced": [
            1
          ]
        }
      }
    ]
  }
}
```

This is the value that's used when querying the index, and it's a long value, and that's the reason our queries did not return the document with 1.1 in the `long_field_coerced` field.

## Some more coercing

Does this coercion only work with numerics? Indeed not. For example, it will also work with strings.

```edql
# This will not work
PUT /mc-test-index/_create/2
{
  "long_field": "2",
  "long_field_coerced": "2"
}
# But this will work and store the value as a long
PUT /mc-test-index/_create/2
{
  "long_field": 2,
  "long_field_coerced": "2"
}
```

Coercing the values might be helpful when the data is coming from different sources and you want to be more tolerant to the incoming formats. However, it's important to remember that the `coerce` parameter can and will lead to data loss in some cases. In our case, for example, the value 1.1 was coerced to 1, which loses precision and might not be what you want. So, use the `coerce` parameter with caution and make sure you understand all the implications.