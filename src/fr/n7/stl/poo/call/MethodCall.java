package fr.n7.stl.poo.call;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.poo.methode.Methode;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.declaration.PooDeclaration;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class MethodCall implements Expression {

    Expression expression;
    String methode;
    List<Expression> parametres;
    Type type;

    public MethodCall( String methode, Expression expression){
        super();
        this.expression = expression;
        this.methode = methode;
    }

    public MethodCall( String methode, List<Expression> parametres){
        super();
        this.parametres = parametres;
        this.methode = methode;
    }

    public  MethodCall(Expression expression, String methode, List<Expression> parametres){
        super();
        this.expression = expression;
        this.methode = methode;
        this.parametres = parametres;
    }

    public MethodCall(Type type,String methode, List<Expression> parametres) {
        this.methode = methode;
        this.parametres = parametres;
        this.type = type;

    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = true;
        if(this.expression != null){
            result = this.expression.completeResolve(_scope);

        if(this.expression instanceof Instanciation){
            Instanciation inst = (Instanciation) this.expression;

            if(_scope.knows(inst.getName())){
                if(_scope.get(inst.getName()) instanceof PooDeclaration){
                    PooDeclaration cld = (PooDeclaration) _scope.get(inst.getName());
                    cld = (PooDeclaration) _scope.get(cld.getName());
                    for(Methode a : cld.getStaticMethodesOfClass()){
                        this.type = a.getType();
                        if(a.getName().equals(this.methode)){
                            return result;
                        }
                    }

                    Logger.error("The name of this methode does not exist or not static methode");
                }else{
                    Logger.error("class doesn't exist");
                }
            }
            }else{
                Type typeClasse = this.expression.getType();

                if(typeClasse instanceof ClasseDeclaration){
                    ClasseDeclaration cld = (ClasseDeclaration) typeClasse;
                    cld = (ClasseDeclaration) _scope.get(cld.getName());
                    for(Methode m : cld.getMethodsOfClass())
                        if(m.getName().equals(this.methode)){
                            this.type = m.getType();
                            return result;
                        }
                    Logger.error("The name of this method does not exist");
                }
                else if(typeClasse instanceof Instanciation){
                    Instanciation instanciation = (Instanciation) typeClasse;
                    PooDeclaration p = (PooDeclaration) _scope.get(instanciation.getName());
                    ClasseDeclaration c =  (ClasseDeclaration) p.getContainer();
                    for(Methode m : c.getMethodsOfClass())
                        if(m.getName().equals(this.methode)){
                            this.type = m.getType();
                            return result;

                        }
                    Logger.error("The name of this method does not exist");
                }
                else{
                    Logger.error("It s not an object");
                }
            }
        }



        return result;


    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {

        Fragment frag = _factory.createFragment();
        for(Expression expr : this.parametres) {
            frag.append(expr.getCode(_factory));
        }

   //     frag.add(_factory.createCall("function_"+this.methode, Register.LB));
        frag.add(_factory.createCall("FUNC_"+this.methode+"_START", Register.LB));

        return frag;
    }
}
