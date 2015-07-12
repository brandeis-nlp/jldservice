/**
    LAPPS JS    lapps JSON processing used functions.
    Copyright   Chunqi SHI (https://github.com/chunqishi)
    License     MIT / http://bit.ly/mit-license
    Version     v0.0.1
*/

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> //
// Services
var execService = function (wsdl, op, inputIds, outputId, alertId, setCodeMirrorValue) {
    var io = {};
    io.Wsdl = wsdl;
    io.Op = op
    io.Input = [];
    // push inputId value into io input
    for (var i = 0; i < inputIds.length; i++) {
        io.Input.push(text2lappsjson(getCodeMirrorValue(inputIds[i])));
    }
    // clear outputId value
    setCodeMirrorValue(outputId, '')

    var req = {};
    req.io = JSON.stringify(io);
    $.ajax({
        type: "POST",
        dataType: "text",
        url: "callservice.groovy",
        data: req,
        success: function(data, dataType) {
          var resJson = jQuery.parseJSON(data);
          if (resJson.Output) {
                setCodeMirrorValue(outputId, JSON.stringify(jQuery.parseJSON(resJson.Output),null,4));
          }
          if(resJson.Except.indexOf('Success') < 0) {
            console.error("Server Error:" + resJson.Except);
            $("#"+alertId).html("<div  class='alert alert-warning' role='alert'><strong>Service Error!</strong>  <a href='#' class='alert-link'>Exception:</a> "+ resJson.Except + "</div>");
                setTimeout(function(){
                    $("#"+alertId).html("");
                }, 20000);
          } else {
            $("#"+alertId).html("<div  class='alert alert-success' role='alert'><strong>Well done!</strong> You successfully exec <a href='#' class='alert-link'>the service</a>.</div>");
            setTimeout(function(){
                $("#"+alertId).html("");
            }, 5000);
          }
         }
    });
}

var execJava = function (classname, op, inputs, callback) {
    var io = {};
    io.ClassName = classname;
    io.Op = op
    io.Input = inputs
    // clear outputId value
    var req = {};
    req.io = JSON.stringify(io);
    $.ajax({
        type: "POST",
        dataType: "text",
        url: "execjava.groovy",
        data: req,
        success: function(data, dataType) {
          var resJson = jQuery.parseJSON(data);
          callback(resJson.Output);
          if (resJson.Output) {
             // console.info("RET:" + resJson.Output);
          }
          if(resJson.Except.indexOf('Success') < 0) {
            //
            console.error("Server Error:" + resJson.Except);
          }else{
            //
          }
        },
        error: function(x, t, m) {
            console.error("ERROR:" + x);
        }
    });
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> //
// CodeMirror
var changeMode = function(editor, value) {
    if (value.trim().substr(0, 1) == "{" && editor.getOption("mode") != "javascript") {
        editor.setOption("mode", "javascript");
        CodeMirror.autoLoadMode(editor, "javascript");
    }
}

var codeMirrors = {};

var coverCodeMirror = function(textareaId) {
    if("undefined"==typeof CodeMirror)
        throw new Error("Lapps JavaScript requires CodeMirror(http://codemirror.net/)");
    var textarea = document.getElementById(textareaId);
    var editor = CodeMirror.fromTextArea(textarea, {
        mode: "javascript",
        lineNumbers: true,
        lineWrapping: true,
        styleActiveLine: true,
        matchBrackets: true,
        scrollbarStyle: "overlay",
        foldGutter: true,
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]
    });
    // editor.setOption("theme", "neat");
    editor.setSize(textarea.style.width, textarea.style.height);
    codeMirrors[textareaId] = editor;
    editor.on('dblclick', function() {
        editor.setOption("fullScreen", !editor.getOption("fullScreen"));
    });
    editor.on("viewportChange", function(){
        editor.refresh();
    });
    return editor;
}

var getCodeMirrorValue = function(textareaId) {
    return codeMirrors[textareaId].getValue();
}

var setCodeMirrorValue = function(textareaId, val) {
    codeMirrors[textareaId].setValue(val);
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  //

var escapeXml = function(unsafe) {
    return unsafe.replace(/[<>&'"]/g, function (c) {
        switch (c) {
            case '<': return '&lt;';
            case '>': return '&gt;';
            case '&': return '&amp;';
            case '\'': return '&apos;';
            case '"': return '&quot;';
        }
    });
}

var formatXml = function(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/[*])/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function(index, node) {
    var indent = 0;
    if (node.match( /.+<\/\w[^>]*>$/ )) {
        indent = 0;
    } else if (node.match( /^<\/\w/ )) {
        if (pad != 0) {
            pad -= 1;
        }
    } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
        indent = 1;
    } else {
        indent = 0;
    }

    var padding = '';
    for (var i = 0; i < pad; i++) {
        padding += ' ';
    }

    formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}