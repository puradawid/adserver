<script>
    var page_number = 1;
    
    var form_category = 'all';
    
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
    
    
    function getAd()
    {
        var request = new XMLHttpRequest();
        
        var querystring = "category="+form_category;
        
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
    <button onClick="nextPage(this);getAd()">Next</button>
</div>

<div id="screen_3" hidden>
    Ok, please paste this link from here:
    <div>
    <textarea rows="50" cols="60" id="quote"></textarea>
    </div>
</div>