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

    var querystring = "category=" + form_category;
    querystring += "&orientation=" + form_orientation;
    querystring += "&media=" + form_media_type;

    request.open("GET", "../ad?" + querystring, false);

    request.send();

    if (request.status != 200)
        alert("something goes wrong");
    document.getElementById("quote")
            .value = request.responseText;
}