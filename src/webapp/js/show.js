var SEARCH_URL = "/mup/action/search";
var STATIC_FOLDER = "/mup/static/";

var getRadioValue = function(radioName){
    var radio = document.getElementsByName(radioName);
    for(i = 0; i<radio.length; i++){
        if (radio[i].checked){
            return radio[i].value
        }
    }
}

var copyClick = function(copyText, button){
    return () => {
        navigator.clipboard.writeText(copyText);
        button.innerText = "copied"
    };
}

window.onload = function(){
    var url_string = window.location.href;
	const params = new URL(url_string).searchParams;

    if (params.size > 0) {
        document.getElementById("searchText").value = params.get("mid");
        loadData()
    }
};

var loadData = function() {
    var searchText = document.getElementById("searchText").value;
    var searchWay = getRadioValue("way");
    const options = { method: 'POST' }
    fetch(SEARCH_URL + "?text=" + searchText + "&way=" + searchWay, options)
        .then(response => {
            if (!response.ok)
                throw new Error('Network response was not ok');
            return response.json();
        }).then(data => {
            showData(data.datas);
        }) .catch(error => {
             console.error('Error fetching JSON:', error);
             window.alert("服务器错误。请联系管理员");
             showData([]);
        });
}

var showData = function(datas) {
    var table = document.getElementById("show");
    var removeNode = function(node) {
        if (node.id !== "head"){
            node.remove();
        }
    };
    Array.from(table.children).forEach(removeNode);

    if (datas.length == 0){
        datas = [{mid: "无数据", name: 0, duration: "无数据"}]
    }

    for (let i = 0; i < datas.length; i++) {
        const data = datas[i]

        var mid = data.mid;
        var url = STATIC_FOLDER + data.name;
        var duration = data.duration;
        
        const td_mid = document.createElement("td");
        td_mid.innerText = mid;

        var copid = document.createElement("button");
        copid.innerText = "copy";
        copid.className = "copy"
        copid.onclick = copyClick(mid, copid)
        td_mid.appendChild(copid);

        const td_url = document.createElement("td");
        td_url.innerHTML = (data.name === 0 ? "无数据" : `<a href="${url}">${data.name}</a>`);
        
        var coptx = document.createElement("button");
        coptx.innerText = "copy";
        coptx.className = "copy"
        coptx.onclick = copyClick((data.name === 0 ? "无数据" : url), coptx)
        td_url.appendChild(coptx);

        const td_durat = document.createElement("td");
        td_durat.innerText = duration;

        var copdu = document.createElement("button");
        copdu.innerText = "copy";
        copdu.className = "copy"
        copdu.onclick = copyClick(duration, copdu)
        td_durat.appendChild(copdu);

        const tr = document.createElement("tr");
        tr.appendChild(td_mid);
        tr.appendChild(td_url);
        tr.appendChild(td_durat);

        table.appendChild(tr)
    }
}