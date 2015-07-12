$(function(){ // on dom ready
  $('#cytodisplay').cytoscape({

    minZoom: 0.5,
    maxZoom: 2,
    hideEdgesonViewport: true,

    style: cytoscape.stylesheet()
    .selector('node')
    .css({
      'width' : 'mapData(degree, 0, 15, 8, 20)',
      'height' : 'mapData(degree, 0, 15, 8, 20)',
      'background-color' : '#2ecc71'
    })
    .selector('node.hover')
    .css({
      'color' : '#2c3e50',
      'content' : 'data(id)',
      'opacity' : 1,
      'background-color' : '#e67e22',
      'z-index' : 101
    })
    .selector('node.hover_neighbor')
    .css({
      'opacity' : 0.8,
      'background-color' : '#f1c40f',
      'z-index' : 100
    })
    .selector('node.cxttap')
    .css({
      'background-color' : '#000000',
      'opacity' : 1
    })
    .selector('edge.hover')
    .css({
      'line-color' : '#e67e22',
      'opacity' : 1,
      'z-index' : 100
    })
    .selector('edge')
    .css({
      'opacity' : '0.5',
      'width' : 'mapData(weight, 0.5, 1, 1, 3)',
      'line-color' : '#95a5a6'
    }),

    // specify the elements in the graph
    elements: {
      nodes: node,
      edges: edge
    },

    layout: {
      name: 'arbor'
    },

    ready: function(){
      window.cy = this;
      cy.elements().unselectify();

      var nodes = cy.nodes();

      var degmap = {};
      var nodes = cy.nodes();
      for (var i = 0; i < nodes.length; i++) {
        degmap[nodes[i].id()] = {degree: nodes[i].degree()};
      }
      cy.batchData(degmap);

      nodes.on('mouseover', function(e){
        var node = e.cyTarget;
        node.addClass('hover');
        node.connectedEdges().addClass('hover');
        node.openNeighborhood().addClass('hover_neighbor');
      });

      nodes.on('mouseout', function(e){
        var node = e.cyTarget;
        node.removeClass('hover');
        node.connectedEdges().removeClass('hover');
        node.openNeighborhood().removeClass('hover_neighbor');
      });

      nodes.on('cxttap', function(e) {
        var node = e.cyTarget;
        if (node.hasClass('cxttap')) {
          node.removeClass('cxttap')
        } else {
          node.addClass('cxttap')
        }
      });

      nodes.qtip({
        content: function(){ return this.data('functions');},
        show: {
          event: 'tap'
        },
        hide: {
          event: 'mouseout'
        },
        position: {
          my: 'top center',
          at: 'bottom center',
          adjust: {
            cyViewport: true
          }
        },
        style: {
          classes: 'qtip-bootstrap',
          tip: {
            width: 16,
            height: 8
          }
        }
      });
    }
  });
});

cytoscape('collection', 'findFunction', function(name) {
  return this.filter(function(i,ele) {
    functions = ele.data("functions") 
    return functions.indexOf(name) >= 0
  });
});
