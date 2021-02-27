package fr.n7.stl.poo.call;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.poo.methode.Constructor;
import fr.n7.stl.block.poo.methode.Methode;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.declaration.Extension;
import fr.n7.stl.poo.declaration.PooDeclaration;
import fr.n7.stl.poo.definition.Attribut;
import fr.n7.stl.poo.definition.Definition;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.*;

public class ConstructorCall implements Expression {

	Instanciation instanciation;

    public Instanciation getInstanciation() {
		return instanciation;
	}
	public void setInstanciation(Instanciation instanciation) {
		this.instanciation = instanciation;
	}

	List<Expression> parametres;


    public ConstructorCall(Instanciation instanciation){
        super();
        this.instanciation = instanciation;

    }
    public ConstructorCall(Instanciation instanciation, List<Expression> parametres){
        super();
        this.parametres = parametres;
        this.instanciation = instanciation;

    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;

        Instanciation in = (Instanciation) this.instanciation;

        result = in.completeResolve(_scope);

        if(!in.getDeclaration().isClasse())
            Logger.error("Cannot use Interface as constructor");

        if(_scope.knows(in.getName())){

            ClasseDeclaration c = (ClasseDeclaration) ((PooDeclaration) _scope.get(in.getName())).getContainer();

            for(Constructor d : c.getConstructorList()){
//                if(d.getConstructor() != null){
                if((d.getParameterDeclarationList() == null && this.parametres != null)
                        || (d.getParameterDeclarationList() != null && this.parametres == null)){
                    Logger.error("Constructor doesn't exist ");
                }
                if(d!= null && this.parametres != null && d.getParameterDeclarationList().size()!= this.parametres.size()){
                    Logger.error("Constructor doesn't exist 2");
                }
                if(this.parametres != null){
                    for(int i = 0 ; i < this.parametres.size();i++){
                        if(! this.parametres.get(i).getType().compatibleWith(d.getParameterDeclarationList().get(i).getType()))
                            Logger.error("Type mismatch error");

                    }
                }
//                }

            }
        }
        if(this.parametres != null){
            for (Expression e : this.parametres){
                result = result && e.completeResolve(_scope);
            }
        }
        return result;
    }
    @Override
    public Type getType() {
        return this.instanciation.getType();
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {

        Fragment frag = _factory.createFragment();
        int id = _factory.createLabelNumber();
        // Empiler les paramètres
        if(this.parametres !=null)
        for (Expression arg : this.parametres){
            frag.append(arg.getCode(_factory));
        }

        // call

      //  Instanciation inst = (Instanciation) this.getType();
        List<Attribut> attributList = new LinkedList<Attribut>();

        if(instanciation.getDeclaration().getDefinitions()!=null){
        List<Definition>  definitionList = instanciation.getDeclaration().getDefinitions();

        for(Definition d : definitionList){
            if(d.getAttribut() != null){
                attributList.add(d.getAttribut());
            }
        }
        }
        int sizeOfAllParams = 0;
        if(attributList.size()!=0){

            for(Attribut a : attributList){
                sizeOfAllParams += a.getType().length();
            }
        }
        sizeOfAllParams +=1;

        frag.add(_factory.createLoadL(sizeOfAllParams));
        frag.add(Library.MAlloc);
        frag.add(_factory.createCall("FUNC_"+this.instanciation.getName()+"_START", Register.SB));
        //frag.add(_factory.createJump("FUNC_"+inst.getName()+"_"+1+"_START"));
        frag.addComment("Function "+this.instanciation.getName()+" call");

        return frag;
    }
}
