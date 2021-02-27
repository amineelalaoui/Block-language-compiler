package fr.n7.stl.poo.definition;

import java.util.List;

import com.sun.xml.internal.bind.v2.TODO;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.Return;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.poo.methode.Constructor;
import fr.n7.stl.block.poo.methode.Methode;
import fr.n7.stl.block.poo.methode.MethodeSignature;
import fr.n7.stl.poo.call.ConstructorCall;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import sun.rmi.runtime.Log;

public class Definition implements Declaration {
    boolean publicOrPrivate;    // true if public, false if private
    boolean isStatic;
    int finalOrAbstract;   /** finalorAbstract : egal à 2 si final, egal à 1 si abstract, egal à 0 si rien*/
    Attribut attribut;
    Methode methode;
    Register register;
    int offset;
    Constructor constructor;

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public  Definition(boolean publicOrPrivate, boolean isStatic, int finalOrAbstract, Methode methode){
        this.publicOrPrivate = publicOrPrivate;
        this.isStatic = isStatic;
        this.finalOrAbstract = finalOrAbstract;
        this.methode= methode;
        if(this.finalOrAbstract == 1)
            if(this.methode.getBlock() != null){
                Logger.error("This Method "+methode.getName()+"() is Abstract You can't define it");
            }
        //TODO
        else
            this.methode.setStatic(isStatic);
    }
    public  Definition(boolean publicOrPrivate, boolean isStatic, int finalOrAbstract,Attribut attribut){
        this.publicOrPrivate = publicOrPrivate;
        this.isStatic = isStatic;
        this.finalOrAbstract = finalOrAbstract;
        this.attribut = attribut;
        this.attribut.setStatic(isStatic);
        this.attribut.publicOrPrivate = this.publicOrPrivate;

    }
    

    public  Definition(boolean publicOrPrivate, Constructor constructor){
        this.publicOrPrivate = publicOrPrivate;
        this.constructor = constructor;

    }

    public Definition() {
    }

    public boolean isPublicOrPrivate() {
        return publicOrPrivate;
    }

    public void setPublicOrPrivate(boolean publicOrPrivate) {
        this.publicOrPrivate = publicOrPrivate;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public int getFinalOrAbstract() {
        return finalOrAbstract;
    }

    public void setFinalOrAbstract(int finalOrAbstract) {
        this.finalOrAbstract = finalOrAbstract;
    }

    public Attribut getAttribut() {
        return attribut;
    }

    public void setAttribut(Attribut attribut) {
        this.attribut = attribut;
    }

    public Methode getMethode() {
        return methode;
    }

    public void setMethode(Methode methode) {
        this.methode = methode;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public String getName() {
    	 if(this.attribut != null ) {
    		 return this.attribut.ident;
    	 }else if(this.methode != null ) {
    		 return this.methode.getName();
    	 }else if(this.constructor != null ) {
    		 return this.constructor.getName();
    	 }else {
    		 Logger.error(" erreur s'est produit");
    		 return null;
    	 }
    }


    public boolean completeResolve(HierarchicalScope<Declaration> _scope,  List<MethodeSignature> methodeSignatureList,
                                   List<Constructor> constructorList
    ){
        boolean result= true;
        if(this.attribut != null ){
            if(this.attribut.expression instanceof ConstructorCall)
                if(constructorList.size() == 0){

                    Logger.error("Constructor not Defined Cannot Instanciate");
                }
            //                System.out.println(this.attribut);
            return  this.attribut.completeResolve(_scope);
        }
        if(this.methode != null ) {
            boolean flag = true;
            if (methodeSignatureList.size() == 0) {
                methodeSignatureList.add(methode.getEntete());{
                    if(this.getType().equals(AtomicType.VoidType)) {
                        for(Instruction i : this.methode.getBlock().getInstructions()) {
                            if(i instanceof Return) {
                                Logger.error("method void can't have a return statement !");
                            }
                        }
                    }
                    return this.methode.completeResolve(_scope);


                }

            } else {
                for (MethodeSignature m : methodeSignatureList) {

                    if (m.getName().equals(methode.getEntete().getName())) {
                        if (!m.getType().equals(methode.getEntete().getType())) {
                            methodeSignatureList.add(methode.getEntete());
                            return result = this.methode.completeResolve(_scope);
                        } else {
                            int size1 = m.getParameterDeclarations().size();
                            int size2= methode.getEntete().getParameterDeclarations().size();

                            if (! (size1 == size2) ){
                                methodeSignatureList.add(methode.getEntete());
//								System.out.println(methodeSignatureList);
                                //    System.exit(0);
                                return result = this.methode.completeResolve(_scope);
                            } else {
                                for (int i = 0; i < size1 && flag; i++) {
                                    Type type1 = m.getParameterDeclarations().get(i).getType();
                                    Type type2 = methode.getEntete().getParameterDeclarations().get(i).getType();
                                    if (!(type1.equals(type2))) {
                                        flag = false;
                                    }
                                }
                                if (!flag) {
                                    methodeSignatureList.add(methode.getEntete());
                                    return result = this.methode.completeResolve(_scope);
                                } else {
                                    Logger.error("Method with params already exist 'from Definition'!!");
                                }
                            }
                        }
                    }else {
                        return result = this.methode.completeResolve(_scope);
                    }
                }

            }
        }
        if(this.constructor != null ) {
            boolean flag = true;
            if (constructorList.size() == 0) {
                constructorList.add(constructor);
                return result = this.constructor.completeResolve(_scope);

            } else {

                for (Constructor c : constructorList) {

                    if (c.getName().equals(constructor.getName())) {
                        int size1 = 0;
                        int size2 = 0;
                        if(c.getParameterDeclarationList() == null) {
                            size1 = 0;
                        }else{
                            size1 = c.getParameterDeclarationList().size();
                        }
                        if(constructor.getParameterDeclarationList() == null) {
                            size2 = 0;
                        }else {
                            size2 = constructor.getParameterDeclarationList().size();
                        }
                        if (! (size1 == size2) ){
                            constructorList.add(constructor);
                            return result = this.constructor.completeResolve(_scope);
                        } else {
                            if(size2 == 0){
                                flag = false;
                            }else{
                                for (int i = 0; i < size1 && flag; i++) {
                                    Type type1 = c.getParameterDeclarationList().get(i).getType();
                                    Type type2 = constructor.getParameterDeclarationList().get(i).getType();
                                    if (!(type1.equals(type2))) {
                                        flag = false;
                                    }
                                }}
                            if (!flag) {
                                constructorList.add(constructor);
                                return result = this.constructor.completeResolve(_scope);
                            } else {
                                Logger.error("constructor with params already exist from Definition'!!");
                            }
                        }

                    }else {
                        return result = this.constructor.completeResolve(_scope);
                    }
                }

            }
        }
        return result;

    }
    public Type getType(){
    	if(this.methode != null) 
			return this.methode.getType();
		else if(this.attribut != null)
			return this.attribut.getType();
		else if(this.constructor != null)
			return this.constructor.getType();
		else {
			Logger.error(" erreur s'est produit 2");
   		 	return null;
		}
    }

    public Fragment getCode(TAMFactory _factory){
        throw  new SemanticsUndefinedException("Semantics getCode is not implemented in Definition POO.");
    }

    public int allocateMemory(Register register, int offset){

        this.register = register;


        if(this.attribut != null){
            return this.attribut.allocateMemory(register,offset);
        }
        else if(this.methode != null){
            return this.methode.allocateMemory(register,offset);
        }
        //Sinon constructeur

        return
                this.constructor.allocateMemory(register,offset);
    }
    public boolean checkType() {
    	if(this.methode != null) 
			return this.methode.checkType();
		else if(this.attribut != null)
			return this.attribut.checkType();
		else if(this.constructor != null)
			return this.constructor.checkType();
		else {
			Logger.error(" erreur s'est produit 2");
   		 	return false;
		}
			
    }

    }
