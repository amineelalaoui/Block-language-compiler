package fr.n7.stl.poo.definition;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.poo.call.ConstructorCall;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.declaration.ContainerDeclaration;
import fr.n7.stl.poo.declaration.InterfaceDeclaration;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.poo.type.PooType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class Attribut implements Declaration, Instruction {
    Type type;
    boolean publicOrPrivate;    // true if public, false if private
    String ident;
    Expression expression;
    boolean isFinal;
    boolean isStatic;
    Register register ;
    int offset;

    public Attribut(Type type, String ident){
        this.type = type;
        this.ident  = ident;
    }

    public Attribut(Type type, String ident, Expression expression, boolean isFinal) {
        this.type = type;
        this.ident = ident;
        this.expression = expression;
        this.isFinal = isFinal;
    }

    public String toSring() {
    	return this.type + " " + this.ident + " = " + this.expression;
    }
    
    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

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

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean result = this.type.completeResolve(_scope);
        if(this.expression != null){
        	
            result = result & expression.completeResolve(_scope);
            //Declaration d = new VariableDeclaration(this.ident, this.type, this.expression);
            if(_scope.accepts(this)){
                _scope.register(this);
                result = result & this.expression.completeResolve(_scope);
            }else{
                Logger.error("This attribut " + this.ident + " already exists");
            }
        }else{
            //Declaration d = new VariableDeclaration(this.ident, this.type, null);
            if(_scope.accepts(this)){
                _scope.register(this);
            }else{
                Logger.error("This attribut " + this.ident + " already exists");
            }

        }


        return result;

    }
    @Override
    public int allocateMemory(Register _register, int _offset) {
       this.register = _register;
       this.offset = _offset;
        return this.type.length();
    }

	@Override
	public boolean checkType() {
		if(this.expression != null){

            Type t1 = this.expression.getType();
			if(t1 instanceof PooType) {
				boolean result = true;
				if(this.type instanceof Instanciation) {
					Instanciation t2 = (Instanciation)this.type;
					ContainerDeclaration cd = t2.getDeclaration().getContainer();
					ClasseDeclaration cd1 = (ClasseDeclaration) ((ConstructorCall)this.expression).getInstanciation().getDeclaration().getContainer();
					if(cd instanceof InterfaceDeclaration) {
						if(cd1.getImplementations().size() != 0) {
							for(Instanciation i : cd1.getImplementations()) {
								if(i.getName().equals(t2.getName())) {
									result = true;
									return result;
								}
							}
							Logger.error("types incompatible !");
							return false;
						}else {
							Logger.error("types incompatible !");
							return false;
						}
						
					}else if(cd instanceof ClasseDeclaration) {
						if(cd1.getExtension().getInstanciation() != null) {
							result = cd1.getExtension().getInstanciation().getName().equals(t2.getName());
							if(result) {
								return result;
							}
						}
						String nom1 = t2.getDeclaration().getName();
						String nom2 = ((ConstructorCall)this.expression).getInstanciation().getName();
						boolean b = nom1.equals(nom2);
						if(!b) {
							Logger.error("expected " + nom1 + " but found " + nom2);
							return false;
						}else {
							return true;
						}
					}
					
				}
			}
			return this.type.compatibleWith(t1);
		}
		return true;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
        Fragment frag = _factory.createFragment();

        frag.add(_factory.createPush(this.getType().length()));

        if(this.expression != null)
            frag.append(expression.getCode(_factory));

        frag.add(_factory.createStore(this.register, this.offset, this.type.length()));

        return frag;
	}

	@Override
	public String getName() {
		return this.ident;
	}
}
