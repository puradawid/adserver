<script>
    var page_number = 1;
    
    var form_category = 'all';
    var form_orientation = 'horizontal';
    var form_media_type = 'html';
    
    function nextPage(sender)
    {
        var oldpage = document.getElementById("screen_" + page_number++);
        var newpage = document.getElementById("screen_" + page_number);
        
        oldpage.setAttribute("hidden", "");
        newpage.removeAttribute("hidden");
        
    }
    
    function setCategory(category)
    {
        form_category = category.options[category.selectedIndex].value;
    }
    
    function setOrientation(orientation)
    {
        form_orientation = orientation.options[orientation.selectedIndex].value;
    }
    
    function setMediaType(media_type)
    {
        form_media_type = media_type.options[media_type.selectedIndex].value;
    }
    
    
    function getAd()
    {
        var request = new XMLHttpRequest();
        
        var querystring = "category="+form_category;
        querystring += "&orientation=" +form_orientation;
        querystring += "&media="+form_media_type;
        
        request.open("GET", "ad?"+querystring, false);
        
        request.send();
        
        if(request.status != 200)
            alert("something goes wrong");
        document.getElementById("quote")
                .value = request.responseText;
    }
    
</script>
<div id="screen_1">
    Hello and welcome to our editor. You will be introduced to create own ad link. Please follow requests.
    <button onClick="nextPage(this);">Next</button>
</div>

<div id="screen_2" hidden> 
    Please select your category to ad
    <br/>
    <select name="category" onChange="setCategory(this)" onLoad="setCategory(this)">
        <%@include file="/WEB-INF/HTML/categories.jsp" %>
    </select>
    <br />
    <button onClick="nextPage(this)">Next</button>
</div>
    

<div id="screen_3" hidden> 
    Please select your ad orientation
    <br/>
    <select name="orientation" onChange="setOrientation(this)" onLoad="setOrientation(this)">
        <option>horizontal</option>
        <option>vertical</option>
    </select>
    <br />
    <button onClick="nextPage(this)">Next</button>
</div>
    
<div id="screen_4" hidden> 
    Please select your media type
    <br/>
    <select name="media_type" onChange="setMediaType(this)" onLoad="setMediaType(this)">
        <option>html</option>
        <option>image</option>
    </select>
    <br />
    <button onClick="nextPage(this);getAd()">Next</button>
</div>

<div id="screen_5" hidden>
    Ok, please paste this link from here:
    <div>
    <textarea rows="50" cols="60" id="quote"></textarea>
    </div>
</div>