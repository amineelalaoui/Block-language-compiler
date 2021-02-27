package fr.n7.stl.block.poo.methode;
import java.util.*;
import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.Assignment;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.Return;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.call.AttributAssignement;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import sun.rmi.runtime.Log;

public class Methode implements Declaration, Instruction {

    MethodeSignature entete;
    Block block;
    public  int isDefined ;
    boolean isStatic = false;
    public int FinalOrAbstract;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsDefined() {
        return isDefined;
    }

    public void setIsDefined(int isDefined) {
        this.isDefined = isDefined;
    }

    public int getFinalOrAbstract() {
        return FinalOrAbstract;
    }

    public void setFinalOrAbstract(int finalOrAbstract) {
        FinalOrAbstract = finalOrAbstract;
    }

    protected int offset ;
    protected  Register register;
    protected String label;

    public MethodeSignature getEntete() {
        return entete;
    }

    public void setEntete(MethodeSignature entete) {
        this.entete = entete;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Methode(MethodeSignature entete, Block block,int isDefined) {
        this.isDefined = isDefined;
        this.entete = entete;
        this.block = block;
    }
    public Methode(MethodeSignature entete,int isDefined) {
        this.isDefined = isDefined;
        this.entete = entete;
    }
//    public Methode(MethodeSignature entete, int isDefined, int FinalOrAbstract){
//        if(isDefined == 1){
//            if(FinalOrAbstract == 1){
//                Logger.error("This Method is Abstract You can't define it");
//            }
//        }
//        else {
//            if(FinalOrAbstract == 1){
//                this.entete = entete;
//                this.FinalOrAbstract = FinalOrAbstract;
//                this.isDefined = isDefined;
//            }
//        }
//    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        HierarchicalScope<Declaration> tableMethodes = new SymbolTable(_scope);

        if(!tableMethodes.accepts(this)){
            Logger.error("Erreur Resolve Variable Declaration");
            return false;
        }else{
            tableMethodes.register(this);
            HierarchicalScope<Declaration> nvlTable = new SymbolTable(tableMethodes);
            boolean result = true;
            if(this.entete.parameterDeclarations != null)
                for(ParameterDeclaration p : this.entete.parameterDeclarations){
                    result = p.getType().completeResolve(_scope) && result;
                    if(nvlTable.accepts(p)) {
                        nvlTable.register(p);
                    }else {
                        return false;
                    }
                }
            /*on fait appel à collect et puis resolve du block*/
            if(this.block !=null){
            this.block.collectAndPartialResolve(nvlTable);
            this.block.completeResolve(nvlTable);

            if(this.isStatic()){
                for(Instruction i: this.block.getInstructions()){
                    if(this.getName().equals("main") && (i instanceof Return)) {
                        Logger.error("Methode main can't have a return !");
                        return false;
                    }
                    if(i instanceof Assignment){
                        Assignment assignment = (Assignment) i;
                        //System.out.println(((Assignment) i).getAssignable());
                        if(assignment.getAssignable() instanceof AttributAssignement){
                            AttributAssignement access = (AttributAssignement) assignment.getAssignable();
                            if(!access.getAttribut().isStatic()){
                                Logger.error("Can't access to not static attribut inside static methode");
                            }
                        }
                        if(assignment.getValue() instanceof AttributAssignement){
                            AttributAssignement access = (AttributAssignement) assignment.getValue();
                            if(!access.getAttribut().isStatic()){
                                Logger.error("Can't access to not static attribut inside static methode");
                            }
                        }

                    }else if (i instanceof VariableDeclaration){
                        VariableDeclaration variableDeclaration = (VariableDeclaration) i;
                        if(variableDeclaration.getValue() instanceof AttributAssignement) {
                        	AttributAssignement access = (AttributAssignement) variableDeclaration.getValue();
                        	if(!access.getAttribut().isStatic()) {
                        		Logger.error("Can't access to not static attribut inside static methode2");
                        	}
                        }
                    }
                }
            }}
            return result;
        }

    }
    public Type getType(){
        return this.entete.getType();
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {

        if(!this.getName().equals("main")){
            this.register = _register;
            this.offset = _offset;
            List<ParameterDeclaration> ListOfParamsReversed = new LinkedList<>();

            int paramsize = 1 ;

            if(this.getEntete().getParameterDeclarations() != null){

                ListOfParamsReversed = new LinkedList<>(this.getEntete().getParameterDeclarations());

                Collections.reverse(ListOfParamsReversed);

                for(ParameterDeclaration p : ListOfParamsReversed){
                    paramsize += p.allocateMemory(Register.LB, -1*paramsize);
                }

            }
            if(this.block!=null)
            this.block.allocateMemory(Register.LB, 3);


        }
        else {
            this.register = _register;
            this.offset = _offset;
            this.block.allocateMemory(register, offset);
            return 0;
        }
        return 0;


    }

	@Override
	public boolean checkType() {
		boolean result = true;
		if(this.getBlock() != null){
		if(!this.getType().equalsTo(AtomicType.VoidType)) {

			Type type = this.block.getTypeOfReturn();
			result = result & this.block.checkType() & this.getType().equalsTo(type);
		}else {
			result = result & this.block.checkType();
		}}
		return result;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
        //get code for main
        this.id = Integer.toString(_factory.createLabelNumber());

        int id = _factory.createLabelNumber();

        if(this.getEntete().getName().equals("main"))
        {

            return this.block.getCode(_factory);
        }
        else {
            Fragment _fragment = _factory.createFragment();

            //By default there is always the pointed object
            int _paramssize = 0;

            if(this.getEntete().getParameterDeclarations() != null)
            for (ParameterDeclaration _parameter : this.getEntete().getParameterDeclarations()) {
                _paramssize += _parameter.getType().length();
            }
            if(this.block!=null)
            _fragment.append(this.block.getCode(_factory));


            if (!this.getEntete().getType().equals(AtomicType.VoidType)) {
                _fragment.add(_factory.createReturn(this.getEntete().getType().length(), _paramssize));
            }


            this.label = "function_" + this.getEntete().getName();
            if(this.block == null){
                _fragment.add(_factory.createPush(0));
            }
            _fragment.addPrefix(this.getStartLabel());
            _fragment.addSuffix(this.getEndLabel());
            Fragment frag_jump = _factory.createFragment();
            frag_jump.add(_factory.createJump(this.getEndLabel()));

            // POP des paramètre (déjà fait par Return)

            frag_jump.append(_fragment);

            return frag_jump;
        }
    }
    public String getStartLabel(){
        if (this.id == null){
            Logger.error("Function label was call before function is declared");
        }

        return "FUNC_"+this.getName()+"_START";
    }
    public String getEndLabel(){
        if (this.id == null){
            Logger.error("Function label was call before function is declared");
        }
        return "FUNC_"+this.getName()+"_END";
    }
	@Override
	public String getName() {
		return this.entete.name;
	}
}
