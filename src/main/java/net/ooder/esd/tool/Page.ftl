ood.Class('${className}', 'ood.Module',{
   Instance:{

        Dependencies:[],
        Required:[],

        properties : ${panel.properties.toJson()},
        initialize : function(){
        },
        iniComponents : function(){

   var host=this, children=[], properties={},
   addChild=function(item,child){
               if (item.children){
                       ood.each(item.children, function(citem){
                          var cchild= ood.create(citem.key)
                          .setHost(host,citem.host)
                          .setAlias(citem.alias)
                          .setEvents(citem.events)
                          .setProperties(citem.properties)
                           if (citem.target){
                                child.append(cchild,citem.target);
                          }else{
                                child.append(cchild);
                          }
                          addChild(citem,cchild);
                       })
                   }
           return child;
       },
       intProperties=function(properties){
                ood.each(properties, function(item){
                var child= ood.create(item.key)
                .setHost(host,item.host)
                .setAlias(item.alias)
                 .setEvents(item.events)
                .setProperties(item.properties)
                 addChild(item,child);
                 children.push(child.get(0) );

                });
          },
                append=function(child){
                    children.push(child.get(0));
                };


            ood.merge(properties, this.properties);


            intProperties(${panel.genChildrenJSON()});

            return children;
            // ]]Code created by CrossUI RAD Studio
        },

        // 可以自定义哪些界面控件将会被加到父容器中
        customAppend : function(parent, subId, left, top){
            return false;
        }
        /*,
        // 属性影响本模块的部分
        propSetAction : function(prop){
        },
        // 本模块中所有ood dom节点的定制CSS style
        customStyle:{}
    },
    // 制定义模块的默认属性和事件声明
    Static:{
        $DataModel:{
        },
        $EventHandlers:{
        }
    */
    }
});