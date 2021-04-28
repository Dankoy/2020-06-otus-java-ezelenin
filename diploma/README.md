Курсовая работа на тему кластерный анализ геоданных.

### ТЗ

1) База данных
    1. БД postgres в docker
    2. В базе одна таблица с 44 столбцами
    3. Каждая строка таблицы - это событие ДТП
    4. Структура таблицы:
       report_number VARCHAR (100), local_case_number VARCHAR(255), agency_name VARCHAR(255), acrs_report_type VARCHAR(
       255), crash_date_time DATE, hit_run VARCHAR(5), route_type VARCHAR(255), mile_point DECIMAL, mile_point_direction
       VARCHAR(10), lane_direction VARCHAR(10), lane_number INTEGER, lane_type VARCHAR(255), number_of_lanes INTEGER,
       direction VARCHAR(10), distance DECIMAL, distance_unit VARCHAR(10), road_grade VARCHAR(255), nontraffic VARCHAR(
       5), road_name VARCHAR(255), cross_street_type VARCHAR(100), cross_street_name VARCHAR(255), off_road_description
       VARCHAR(255), municipality VARCHAR(100), related_non_motorist VARCHAR(50), at_fault VARCHAR(20), collision_type
       VARCHAR(100), weather VARCHAR(30), surface_condition VARCHAR(50), light VARCHAR(30), traffic_control VARCHAR(50),
       driver_substance_abuse VARCHAR(150), non_motorist_substance_abuse VARCHAR(150), first_harmful_event VARCHAR(100),
       second_harmful_event VARCHAR(100), fixed_object_struck VARCHAR(100), junction VARCHAR(50), intersection_type
       VARCHAR(50), intersection_area VARCHAR(50), road_alignment VARCHAR(30), road_condition VARCHAR(30), road_division
       VARCHAR(150), latitude DECIMAL, longitude DECIMAL, geolocation VARCHAR(150)
    4. Датасет для бд хранится по
       урлу https://data.montgomerycountymd.gov/Public-Safety/Crash-Reporting-Incidents-Data/bhju-22kf

2) Фронтенд
    1. HTML форма с картой google maps. На карте отображаются точки (кластера).
    2. Фронт получает данные для отрисовки (широта и долгота) по ручке предоставляемой бэком. Ручек четыре:
        - кластера по ДТП с пешеходами и велосипедистами, собранные с помощью алгоритма k-means
        - кластера по всем событиям из базы, собранные с помощью алгоритма k-means -* кластера по ДТП с пешеходами и
          велосипедистами, собранные с помощью алгоритма DBSCAN -* кластера по всем событиям из базы, собранные с
          помощью алгоритма DBSCAN
    3. Карту можно скролить, при этом кластера на карте должны "на лету" перерисовываться. 4*. На HTML форме можно
       выбрать какой алгоритм использовать для отрисовки кластеров - выпадающий список + кнопка для запуска алгоритма.

3) Бэкенд
    1. Бэкенд предоставляет для фронта ручку для получения кластеров (алгоритм k-means), при этом предварительно
       фильтрует данные по столбцу в базе related_non_motorist 2*. Бэкенд предоставляет для фронта для получения
       кластеров (алгоритм DBSCAN), при этом предварительно фильтрует данные по столбцу в базе related_non_motorist
    3. Бэкенд должен реализовать алгоритм k-means кластеризации геоданных 4*. Бэкенд должен реализовать алгоритм DBSCAN
       кластеризации геоданных
    5. Бэкенд предоставляет для фронта ручку для получения кластеров (алгоритм k-means) без фильтрации по столбцу в базе
       related_non_motorist, то есть кластера стоятся по всем событиям в базе. 6*. Бэкенд предоставляет для фронта ручку
       для получения кластеров (алгоритм DBSCAN) без фильтрации по столбцу в базе related_non_motorist, то есть кластера
       стоятся по всем событиям в базе. 7*. Для алгоритма DBSCAN точки, относящиеся к кластеру должны отрисовываться
       разными цветами

### Реализовано

Приложение доступно по урлу http://localhost:8080/diploma/

На момент 28-04-2021 выполнены все пункты, кроме пунктов со звездочкой.

2) Фронтенд
    1. HTML форма с картой google maps. На карте отображаются точки (кластера).
    2. Фронт получает данные для отрисовки (широта и долгота) по ручке предоставляемой бэком. Ручек четыре:
        - кластера по ДТП с пешеходами и велосипедистами, собранные с помощью алгоритма k-means
        - кластера по всем событиям из базы, собранные с помощью алгоритма k-means
    3. Карту можно скролить, при этом кластера на карте должны "на лету" перерисовываться. 4*. На HTML форме можно
       выбрать какой алгоритм использовать для отрисовки кластеров - выпадающий список + кнопка для запуска алгоритма.

3) Бэкенд
    1. Бэкенд предоставляет для фронта ручку для получения кластеров (алгоритм k-means), при этом предварительно
       фильтрует данные по столбцу в базе related_non_motorist
    3. Бэкенд должен реализовать алгоритм k-means кластеризации геоданных
    5. Бэкенд предоставляет для фронта ручку для получения кластеров (алгоритм k-means) без фильтрации по столбцу в базе
       related_non_motorist, то есть кластера стоятся по всем событиям в базе.

### Зависимости:

1) docker
2) postgres
3) java 13
4) Датасет https://data.montgomerycountymd.gov/Public-Safety/Crash-Reporting-Incidents-Data/bhju-22kf
5) tomcat

### Первоначальная настройка окружения

1) Установить docker
2) Развернуть БД postgres:
   ```shell
   docker run --name java-diploma -p 5432:5432 -v /home/ezelenin/Documents/docker/postgresql/persistance:/var/lib/postgresql/data -e POSTGRES_PASSWORD=password -e POSTGRES_USER=ezelenin -d postgres
   ```
3) Создать таблицу:

```postgresql
create table crashes
(
    report_number                VARCHAR(100),
    local_case_number            VARCHAR(255),
    agency_name                  VARCHAR(255),
    acrs_report_type             VARCHAR(255),
    crash_date_time              DATE,
    hit_run                      VARCHAR(5),
    route_type                   VARCHAR(255),
    mile_point                   DOUBLE PRECISION,
    mile_point_direction         VARCHAR(10),
    lane_direction               VARCHAR(10),
    lane_number                  INTEGER,
    lane_type                    VARCHAR(255),
    number_of_lanes              INTEGER,
    direction                    VARCHAR(10),
    distance                     DOUBLE PRECISION,
    distance_unit                VARCHAR(10),
    road_grade                   VARCHAR(255),
    nontraffic                   VARCHAR(5),
    road_name                    VARCHAR(255),
    cross_street_type            VARCHAR(100),
    cross_street_name            VARCHAR(255),
    off_road_description         VARCHAR(255),
    municipality                 VARCHAR(100),
    related_non_motorist         VARCHAR(50),
    at_fault                     VARCHAR(20),
    collision_type               VARCHAR(100),
    weather                      VARCHAR(30),
    surface_condition            VARCHAR(50),
    light                        VARCHAR(30),
    traffic_control              VARCHAR(50),
    driver_substance_abuse       VARCHAR(150),
    non_motorist_substance_abuse VARCHAR(150),
    first_harmful_event          VARCHAR(100),
    second_harmful_event         VARCHAR(100),
    fixed_object_struck          VARCHAR(100),
    junction                     VARCHAR(50),
    intersection_type            VARCHAR(50),
    intersection_area            VARCHAR(50),
    road_alignment               VARCHAR(30),
    road_condition               VARCHAR(30),
    road_division                VARCHAR(150),
    latitude                     DOUBLE PRECISION,
    longitude                    DOUBLE PRECISION,
    geolocation                  VARCHAR(150)
)
```

3) Скачать данные https://data.montgomerycountymd.gov/Public-Safety/Crash-Reporting-Incidents-Data/bhju-22kf
4) Импортировать в контейнер с БД

```postgresql
\copy crashes from '/home/ezelenin/Downloads/Crash_Reporting_-_Incidents_Data.csv' DELIMITER ',' CSV HEADER;
```

### Примеры

1) При первом запуске приложения открывается карта с зумом 12, считаются кластера по авариям с пешеходами
   
   ![] (https://github.com/Dankoy/cluster_analyze-images/blob/main/images/Screenshot_2021-04-28-cluster.png?raw=true)

2) Можно выбирать какие данные получать из базы. Для этого в правом верхнем углу страницы есть селектор и кнопка

   ![] (https://github.com/Dankoy/cluster_analyze-images/blob/main/images/Screenshot_2021-04-28-cluster1.png?raw=true)

3) При изменении зума или границ карты кластеры высчитываются заново

   ![] (https://github.com/Dankoy/cluster_analyze-images/blob/main/images/Screenshot_2021-04-28-cluster2.png?raw=true)
   ![] (https://github.com/Dankoy/cluster_analyze-images/blob/main/images/Screenshot_2021-04-28-cluster3.png?raw=true)






