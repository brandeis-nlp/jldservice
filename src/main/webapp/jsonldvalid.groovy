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

function compact() {
    var doc = \$("#jsonld_doc").val();
    var context = \$("#jsonld_context").val();

    jsonld.compact(jQuery.parseJSON(doc), jQuery.parseJSON(context), function(err, compacted) {
        console.log(JSON.stringify(compacted, null, 2));
        \$("#jsonld_compacted").val(JSON.stringify(compacted, null, 2));
        var node = new PrettyJSON.view.Node({
                el:\$("#jsonld_compacted_pretty"),
                data:compacted
        });
    });
}


function valid() {
    var compact_0 = \$("#jsonld_compact_0").val();
    jsonld.expand(jQuery.parseJSON(compact_0), function(err, expanded) {
        console.log(JSON.stringify(expanded, null, 2));
        \$("#jsonld_expanded_0").val(JSON.stringify(expanded, null, 2));
    });
}
</script>

</head>

<body>
    <h2> Please valid the Json </h2>
    <iframe src="http://pro.jsonlint.com/" seamless="seamless" width="62%" scrolling="yes">
    <p>Your browser does not support iframes, please goto <a href="http://pro.jsonlint.com/" target="_blank">JsonLint</a>.</p>
    </iframe>
    <p>
    <hr/>
    <h2> Please valid the Json-LD</h2>
    <table><tr><td>
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
   <textarea id="jsonld_expanded_0" cols="50" rows="20"></textarea></td></tr>
    </table>
    <hr/>
    <h2> Please compact the Json-LD</h2>

    <p>
    <table>
    <tr>
    <th>Document: </th><th> Context:</ht>
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
}</textarea></td><tr/>
    <tr>
    <td colspan="2">
    <p><input type="button" value="Compact" onclick="compact()" />
    <p><b>Compacted Json-LD</b></p>
    </td></tr><tr><td>
    <textarea  id="jsonld_compacted"  cols="50" rows="20"> </textarea>
    </td><td><span id="jsonld_compacted_pretty"></span>
    </td>
    </tr>
    </table>

</body>
</html>

"""