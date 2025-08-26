
xui.Class('${className}', 'xui.Module',{
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
                xui.checkFunction(host.functions);
                xui.checkEvents(host.events);

                getEUPropertis=function(){
                 var euProperties= ${childrenJson};
                 return euProperties;
                } ;
                xui.merge(properties, this.properties);
                children= xui.intModuleProperties(getEUPropertis(),host);
               return children;

            },
<#if afterAppend??>
        afterAppend :  ${afterAppend},
</#if>
        customAppend :  ${customAppendStr}<#if customScriptStr!=null>,
    ${customScriptStr}</#if> <#if moduleVarStr!=null>,
    ${moduleVarStr}
</#if>
   ,rightFormulas:${rightFormulas}
 } ,
Static:${Static}



});