### 2025-07-21
修改了动态路径匹配的方法, 支持不同前缀的参数名称, 并同步至Rust版本

[细节](https://github.com/FTBoojux/plp/blob/main/documents/DevelopmentJournal/250721.pathvariablesForRust.md)

modify the process of dynamic path match, now it support same prefix with different path variables' name

[details](https://github.com/FTBoojux/plp/blob/main/documents/DevelopmentJournal/250721.pathvariablesForRust.en.md)

### 2025-07-22
引入线程池处理请求

[细节](https://github.com/FTBoojux/plp/blob/main/documents/DevelopmentJournal/250722.stressTestOnSingleMachine.en.md)

introduce thread pool to handle the request instead of handling the request in the while loop

### 2025-08-01
引入异步日志打印

introduce asynchronous log printing

[细节](https://github.com/FTBoojux/plp/blob/main/documents/DevelopmentJournal/250801.asynchronousLogPrint.md)

### 2025-08-07
引入JSON序列化和Request Body的自动转换

introduce JSON parser and request body parser

### 2025-08-30
增加了对 form-data 和 x-www-form-urlencoded 格式数据的支持

support transfer data  in the format of form-data and x-www-form-urlencoded

[细节](https://github.com/FTBoojux/plp/blob/main/documents/DevelopmentJournal/250830.formDataAndUrlencoded.md)