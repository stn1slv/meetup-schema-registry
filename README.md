# schema-registry-demo


## Start environment

## Testing
### Send message to Kafka
```
printf ' <?xml version="1.0" encoding="utf-8"?>
<PurchaseOrder xmlns="http://tempuri.org/PurchaseOrderSchema.xsd" 
    xsi:schemaLocation="http://tempuri.org/PurchaseOrderSchema.xsd schema.xsd" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    OrderDate="1974-13-01">
    <ShipTo country="US">
        <name>string</name>
        <street>string</street>
        <city>string</city>
        <state>string</state>
        <zip>217</zip>
    </ShipTo>
    <ShipTo country="US">
        <name>string</name>
        <street>string</street>
        <city>string</city>
        <state>string</state>
        <zip>-724</zip>
    </ShipTo>
    <BillTo>
        <name>string</name>
        <street>string</street>
        <city>string</city>
        <state>string</state>
        <zip>3402</zip>
    </BillTo>
</PurchaseOrder>' | kcat -P -b 127.0.0.1 -t input
```

### Send message via http endpoint

```
printf '{
    "orderDate": "2022-10-04",
    "shipTo": {
        "country": "Serbia",
        "state": "Novi Sad",
        "city": "Novi Sad",
        "street": "Bulevar Jaše Tomića",
        "name": "Novi Sad Train Station"
    },
    "billTo": "for me"
}'| http  --follow --timeout 3600 POST 'http://localhost:8085/doSomething' \
 Content-Type:'application/json'
```

#### Monitor messages in DLQ topic
```
kcat -b 127.0.0.1 -t dlq -f '\nKey: %k\t\nHeaders: %h \t\nValue: %s\\n--\n'
```

#### Monitor messages in Output topic
```
kcat -b 127.0.0.1 -t output
```
