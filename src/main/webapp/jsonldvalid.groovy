println """

<!DOCTYPE html>
<html>
<head>
    <title>JsonValid</title>

    <script src="http://code.jquery.com/jquery-1.10.2.js" >  </script>
    <!--script src="http://code.jquery.com/jquery-latest.min.js"></script-->
    <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js" >  </script>
    <script src="https://rawgithub.com/padolsey/prettyprint.js/master/prettyprint.js" >  </script>
    <script src="https://rawgithub.com/epeli/underscore.string/master/lib/underscore.string.js" >  </script>
    <script src="https://rawgithub.com/patricklodder/jquery-zclip/master/jquery.zclip.js" >  </script>


    <script src="http://warfares.github.io/pretty-json/libs/underscore-min.js" >  </script>
    <script src="http://warfares.github.io/pretty-json/libs/backbone-min.js" >  </script>
    <script src="http://warfares.github.io/pretty-json/pretty-json-min.js" >  </script>


    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/jsonld.js" >  </script>
    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/rdfa.js" >  </script>
    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/request.js" >  </script>
    <script src="https://raw.githubusercontent.com/s3u/JSONPath/master/lib/jsonpath.js" >  </script>


<script type="text/javascript">

function display(id) {
    var json = \$("#" + id).val();
    if (json) {
        var node = new PrettyJSON.view.Node({
                el:\$("#display_" + id),
                data:jQuery.parseJSON(json)
        });
        node.expandAll();
    }
}

function valid() {
    var compact_0 = \$("#jsonld_compact_0").val();
    \$("#jsonld_expanded_0").val("");

    var json_compact_0;
    try
    {
      json_compact_0 = jQuery.parseJSON(compact_0)
    } catch(err) {
      \$("#jsonld_expanded_0").attr("placeholder", "Compacted -->  " + err);
    }
    if (json_compact_0) {
        jsonld.expand(json_compact_0, function(err, expanded) {
            console.log(JSON.stringify(expanded, null, 2));
            if(err) {
                \$("#jsonld_expanded_0").attr("placeholder", "Expand -->  " + JSON.stringify(err, null, 2));
            }
            if (expanded) {
                \$("#jsonld_expanded_0").val(JSON.stringify(expanded, null, 2));
            }
            display("jsonld_compact_0");
            display("jsonld_expanded_0");
        });
    }
}

function compact() {
    var doc = \$("#jsonld_doc").val();
    var context = \$("#jsonld_context").val();
    \$("#jsonld_compacted").val("");

    var json_doc;
    var json_context;
    try
    {
      json_doc = jQuery.parseJSON(doc)
    } catch(err) {
      \$("#jsonld_compacted").attr("placeholder", "Document -->  " + err);
    }

        var json_compact_0;
    try
    {
      json_context = jQuery.parseJSON(context)
    } catch(err) {
      \$("#jsonld_compacted").attr("placeholder", "Context -->  " + err);
    }

    if (json_doc && json_context) {
        jsonld.compact(json_doc, json_context, function(err, compacted) {
            console.log(JSON.stringify(compacted, null, 2));
            if (err) {
                \$("#jsonld_compacted").attr("placeholder",  "Compact -->  " + JSON.stringify(err, null, 2));
            }
            if (compacted) {
                \$("#jsonld_compacted").val(JSON.stringify(compacted, null, 2));
            }
            display("jsonld_doc");
            display("jsonld_context");
            display("jsonld_compacted");
        });
    }
}



</script>

</head>

<body>
    <h1  align="center">JSON Validate</h1>

    <h2> Please Validate the Json </h2>
    <iframe src="http://pro.jsonlint.com/" seamless="seamless" width="62%" scrolling="yes">
    <p>Your browser does not support iframes, please goto <a href="http://pro.jsonlint.com/" target="_blank">JsonLint</a>.</p>
    </iframe>
    <p>
    <!------------------------------------------------------------------------------------>
    <hr/>
    <h2> Please Validate the Json-LD</h2>
    <table>

    <tr>
    <th>Compacted: </th><th></th><th> Expanded:</th>
    </tr><tr><td>
    <textarea id="jsonld_compact_0" cols="50" rows="20">{
  "@context": {
    "name": "http://schema.org/name",
    "homepage": {
      "@id": "http://schema.org/url",
      "@type": "@id"
    },
    "image": {
      "@id": "http://schema.org/image",
      "@type": "@id"
    }
  },
  "image": "http://manu.sporny.org/images/manu.png",
  "name": "Manu Sporny",
  "homepage": "http://manu.sporny.org/"
}</textarea></td><td>
   <input type="button" value="Valid" onclick="valid()" /></td><td>
   <textarea id="jsonld_expanded_0" cols="50" rows="20"></textarea></td></tr><tr><td>
   <div id="display_jsonld_compact_0"></div></td><td></td><td><div id="display_jsonld_expanded_0"></div></td></tr>
    </table>
    <!------------------------------------------------------------------------------------>
    <hr/>
    <h2> Please Compact the Json-LD</h2>

    <p>
    <table>
    <tr>
    <th>Document: </th><th> Context:</th><th> </th><th>Compacted Json-LD</th>
    </tr><tr><td>
    <textarea id="jsonld_doc" cols="50" rows="20">{
  "http://schema.org/name": "Manu Sporny",
  "http://schema.org/url": {"@id": "http://manu.sporny.org/"},
  "http://schema.org/image": {"@id": "http://manu.sporny.org/images/manu.png"}
}</textarea></td><td>
    <textarea id="jsonld_context" cols="50" rows="20">{
  "name": "http://schema.org/name",
  "homepage": {"@id": "http://schema.org/url", "@type": "@id"},
  "image": {"@id": "http://schema.org/image", "@type": "@id"}
}</textarea></td><td>
    <input type="button" value="Compact" onclick="compact()" />
    </td><td>
    <textarea  id="jsonld_compacted"  cols="50" rows="20"> </textarea>
    </td></tr><tr><td>
    <div id="display_jsonld_doc"></div></td><td><div id="display_jsonld_context"></div></td><td></td>
    <td><span id="display_jsonld_compacted"></span>
    </td></tr>
    </table>

</body>
</html>

"""