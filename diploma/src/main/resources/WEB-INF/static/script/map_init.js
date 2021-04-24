const defaultZoom = 9;

const zoom_relations = {
    "3": {
        clusterSize: 3
    }, "4": {
        clusterSize: 4
    }, "5": {
        clusterSize: 5
    }, "6": {
        clusterSize: 6
    }, "7": {
        clusterSize: 7
    }, "8": {
        clusterSize: 30
    }, "9": {
        clusterSize: 30
    }, "10": {
        clusterSize: 30
    }, "11": {
        clusterSize: 28
    }, "12": {
        clusterSize: 28
    }, "13": {
        clusterSize: 26
    }, "14": {
        clusterSize: 30
    }, "15": {
        clusterSize: 25
    }, "16": {
        clusterSize: 21
    }, "17": {
        clusterSize: 16
    }, "18": {
        clusterSize: 15
    }, "19": {
        clusterSize: 15
    }
}

//дефолтное значение выборки данных
let selectedData = "nonmotorist";

/**
 * Получение значения элемента селектора для осуществления запросов к базе с корректными данными
 *
 */
function getSelectedTextValue() {
    const ddlData = document.getElementById("ddlData");
    selectedData = ddlData.value;
}


function initMap() {

    // Инициализация пустого массива маркеров
    var markers = [];

    var map = L.map('mapId');

    var OpenStreetMap_Mapnik = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        minZoom: 3,
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });

    // Initialize with openstreetmap
    OpenStreetMap_Mapnik.addTo(map);
    L.control.scale().addTo(map);

    map.setView([39.0458, -76.6413], defaultZoom);

    // Первоначальная отрисоква карты с маркерами
    printMarkersDelegate(map, markers);

    // Получение зумма и класетров
    // логики в другом методе не сделать
    async function onZoomend() {
        await printMarkersDelegate(map, markers);
    }

    async function onDragend() {
        await printMarkersDelegate(map, markers)
    }

    // Перерисовка карты, когда меняется зум
    map.on('zoomend', onZoomend);

    // Перерисовка карты, когда меняются координаты карты
    map.on('dragend', onDragend);

}

/**
 *
 * Получение кластеров по авариям с пешеходами по размеру кластера
 *
 * @param clusterSize
 * @returns {Promise<string>}
 */
function getClustersByClusterSize(clusterSize) {
    const kmeansNonMotoristByClusterSize = 'nonmotorist/'
    return fetch(pathToApi + kmeansNonMotoristByClusterSize + clusterSize)
        .then(response => response.json())
        .then(cluster => JSON.stringify(cluster))
        .then(cluster => JSON.parse(cluster));

}

/**
 *
 * Получение кластеров по авариям с пешеходами по размеру кластера и по границам карты
 *
 * @param clusterSize количество кластеров
 * @param north координата севера
 * @param south координата юга
 * @param west координата запада
 * @param east координата востока
 * @returns {Promise<string>}
 */
function getClustersByClusterSizeAndMapBounds(clusterSize, north, south, west, east) {
    const kmeansNonMotoristByClusterSize = 'nonmotorist/'

    const finalUrl = pathToApi + kmeansNonMotoristByClusterSize + clusterSize + "?north=" + north + "&south=" + south + "&west=" + west + "&east=" + east;

    return fetch(finalUrl)
        .then(response => response.json())
        .then(cluster => JSON.stringify(cluster))
        .then(cluster => JSON.parse(cluster));

}

/**
 * Получение кластеров по всем событиям ДТП по размеру кластера
 *
 * @param clusterSize
 * @returns {Promise<string>}
 */
function getClustersByClusterSizeAll(clusterSize) {
    const kmeansNonMotoristByClusterSize = 'all/'
    return fetch(pathToApi + kmeansNonMotoristByClusterSize + clusterSize)
        .then(response => response.json())
        .then(cluster => JSON.stringify(cluster))
        .then(cluster => JSON.parse(cluster));

}

/**
 * Получение кластеров по всем событиям ДТП по размеру кластера и по границам карты
 *
 * @param clusterSize
 * @param north координата севера
 * @param south координата юга
 * @param west координата запада
 * @param east координата востока
 * @returns {Promise<string>}
 */
function getClustersByClusterSizeAndMapBoundsAll(clusterSize, north, south, west, east) {
    const kmeansAll = 'all/'

    const finalUrl = pathToApi + kmeansAll + clusterSize + "?north=" + north + "&south=" + south + "&west=" + west + "&east=" + east;
    return fetch(finalUrl)
        .then(response => response.json())
        .then(cluster => JSON.stringify(cluster))
        .then(cluster => JSON.parse(cluster));

}

/**
 * Конвертация списка кластеров в список маркеров для отрисовки на карте. В всплывающее меню маркера добавлен lat,
 * long кластера, а так же количество событий в кластере.
 *
 * @param clusters
 * @param markers
 * @returns {*}
 */
function addMarkersToMap(clusters, markers) {

    markers.length = 0;

    clusters.forEach(
        cluster => {

            var popup = "<b style='font-size:10pt;'>Amount of accidents: </b>" + cluster.points.length +
                "<br> <i style='font-size:7pt;'>Latitude:" + cluster.latitude + "</i>" +
                "<br> <i style='font-size:7pt;'>Longitude:" + cluster.longitude + "</i>";

            // Creating Marker Options
            var markerOptions = {
                title: "Amount of accidents: " + cluster.points.length,
                clickable: true,
                draggable: false,
                riseOnHover: true
            }

            var marker = new L.marker([cluster.latitude, cluster.longitude], markerOptions).bindPopup(popup);

            marker.on('mouseover', function (ev) {
                marker.openPopup();
            });

            markers.push(marker);
        }
    )

    return markers;

}

/**
 * Отрисовка маркеров на карте
 *
 * @param map
 * @param markers
 * @returns {Promise<void>}
 */
async function printMarkersDelegate(map, markers) {

    const currentZoom = map.getZoom();

    const north = map.getBounds().getNorth(); //
    const south = map.getBounds().getSouth();
    const west = map.getBounds().getWest();
    const east = map.getBounds().getEast();

    document.getElementById('zoom').innerHTML = currentZoom;
    document.getElementById('cluster').innerHTML = zoom_relations[currentZoom].clusterSize;

    let clusters;

    if (selectedData === "nonmotorist") {
        clusters = await getClustersByClusterSizeAndMapBounds(zoom_relations[currentZoom].clusterSize, north, south, west, east);
    } else if (selectedData === "all") {
        clusters = await getClustersByClusterSizeAndMapBoundsAll(zoom_relations[currentZoom].clusterSize, north, south, west, east);
    }

    // var clusters = await getClustersByClusterSize(zoom_relations[currentZoom].clusterSize);
    // var clusters = await getClustersByClusterSizeAll(zoom_relations[currentZoom].clusterSize);

    // очистка от старых маркеров
    markers.forEach(marker => {
        map.removeLayer(marker);
    });

    addMarkersToMap(clusters, markers);

    // рисование новых маркеров
    markers.forEach(marker => {
        marker.addTo(map);
    })
}
