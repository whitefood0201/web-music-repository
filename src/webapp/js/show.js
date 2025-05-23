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

function fallbackCopyTextToClipboard(text, button) {
    const textarea = document.createElement("textarea");
    textarea.value = text;
    textarea.style.position = "fixed";
    document.body.appendChild(textarea);
    textarea.focus();
    textarea.select();
    try {
        const successful = document.execCommand('copy');
        button.innerText = successful ? "copied" : "failed";
    } catch (err) {
        console.error("Fallback failed:", err);
        button.innerText = "error";
    }
    document.body.removeChild(textarea);
}

var copyClick = function(copyText, button){
    return () => {
        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(copyText).then(() => {
                button.innerText = "copied";
            }).catch(err => {
                console.error("Clipboard error:", err);
                fallbackCopyTextToClipboard(copyText, button);
            });
        } else {
            fallbackCopyTextToClipboard(copyText, button);
        }
    };
};

var buildCopyBtn = function(text) {
    var btn = document.createElement("button");
    btn.innerText = "copy";
    btn.className = "copy";
    btn.onclick = copyClick(text, btn);
    return btn;
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
    var searchField = getRadioValue("searchField");
    const options = { method: 'POST' }
    fetch(SEARCH_URL + "?text=" + searchText + "&searchField=" + searchField, options)
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
        datas = [
            {mid: "无数据", name: 0, duration: "无数据", artists: ["无", "数", "据"]}
        ]
    }

    for (let i = 0; i < datas.length; i++) {
        const data = datas[i]

        var mid = data.mid;
        var url = STATIC_FOLDER + data.name;
        var duration = data.duration;
        
        // mid
        const td_mid = document.createElement("td");
        td_mid.innerText = mid;
        var copid = buildCopyBtn(mid);
        td_mid.appendChild(copid);

        // url
        const td_url = document.createElement("td");
        td_url.innerHTML = (data.name === 0 ? "无数据" : `<a href="${url}">${data.name}</a>`);
        var coptx = buildCopyBtn((data.name === 0 ? "无数据" : url));
        td_url.appendChild(coptx);

        // artists
        const td_artts = document.createElement("td");
        td_artts.innerHTML = data.artists.join("/");

        // duration
        const td_durat = document.createElement("td");
        td_durat.innerText = duration;
        var copdu = buildCopyBtn(duration)
        td_durat.appendChild(copdu);

        const tr = document.createElement("tr");
        tr.appendChild(td_mid);
        tr.appendChild(td_url);
        tr.appendChild(td_artts);
        tr.appendChild(td_durat);

        table.appendChild(tr)
    }
}