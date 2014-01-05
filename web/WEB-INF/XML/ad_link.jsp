<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    function adGet(sender)
    {
        var req = new XMLHttpRequest();
        var category = sender.getAttribute("category");
        req.open("GET", "http://localhost:8084/AdServer/ad?action=getAd&category="+category+"&orientation=${param.orientation}&media=${param.media}");
        req.onreadystatechange = function(status) {
            if(this.readyState == 4)
                sender.innerHTML = req.responseText;
        }
        req.send();
    
    }
    function gotoAd(sender)
    {
        var id = document.getElementById("ad_id").getAttribute("identification");
        window.open("http://localhost:8084/AdServer/ad?action=redirect&id="+id);
    }
</script>

<div class = 'adverisement' category="${param.category}" id="27893874932"
     style="border:1px solid black; cursor: pointer" onLoad="adGet(this)" onClick="gotoAd()">
    <script>adGet(document.getElementById("27893874932"));</script>
</div>
