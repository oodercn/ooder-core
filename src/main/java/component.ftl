
ood.Class('${className}', 'ood.Module',{
 Instance:{
        initialize : function(){ },
        Dependencies:${dependencies},
        Required:${required},
        properties : ${properties},
        events:${events},
        functions:${functions},
        iniComponents : function(){
                var host=this, children=[], properties={},
                append=function(child){
                    children.push(child.get(0));
                };
                ood.checkFunction(host.functions);
                ood.checkEvents(host.events);

                getEUPropertis=function(){
                 var euProperties= ${childrenJson};
                 return euProperties;
                } ;
                ood.merge(properties, this.properties);
                children= ood.intModuleProperties(getEUPropertis(),host);
               return children;

            },

        customAppend : function(parent, subId, left, top){
            return false;
        }<#if customScriptStr!=null>,
    ${customScriptStr}</#if> <#if moduleVarStr!=null>,
    ${moduleVarStr}
</#if>
   ,rightFormulas:${rightFormulas}
 } ,
Static:${Static}



});