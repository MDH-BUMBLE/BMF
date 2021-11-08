concept PssModel  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
abstractcomponent : AbstractComponent[]references:
 
concept AbstractCom  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
action concept : Action concept[one to many]references:
 abstractcomponent associate : AbstractComponent associate[one to many]
concept Component extends AbstractComponent implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
dataobject : DataObject[]references:
 
concept RootComponent extends Component implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 componentinvocation concept
			
			actioninvocation : ComponentInvocation concept
			
			ActionInvocation[1]
concept ComponentIn  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 component : Component[]
concept ActionInvoc  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 action concept : Action concept[]
concept Action i extends ActionInvocation concept implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
activity concept
			
			object concept : Activity concept
			
			Object concept[]references:
 
concept Activity  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
componentinvocation concept : ComponentInvocation concept[]references:
 
concept Object i extends DataObject concept implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept DataFlowObj extends Stream concept and buffer concept implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept Stream  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept Buffer h  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept DataFlowObj extends Resource concept implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept Resource extends Pool concept implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 
concept Pool as  extends BaseConcept  implements <none>
instance can be root: false
alias: <no alias>
short description: <no short description>
properties:
children:
references:
 resource concept : resource concept[1]
