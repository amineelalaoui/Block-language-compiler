package fr.n7.stl.block.ast.instruction;

import java.util.ArrayList;
import java.util.List;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.poo.methode.Constructor;
import fr.n7.stl.poo.declaration.ClasseDeclaration;
import fr.n7.stl.poo.definition.Definition;
import fr.n7.stl.poo.type.Instanciation;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class SuperClass implements Instruction{

	protected Instanciation instanciation;
	public Instanciation getInstanciation() {
		return instanciation;
	}

	public void setInstanciation(Instanciation instanciation) {
		this.instanciation = instanciation;
	}

	protected ClasseDeclaration classeMere;
	protected Constructor constructor;
	List<Expression> parametres = new ArrayList<Expression>(); //les parametres de super
	List<ParameterDeclaration> parameterDeclarationList;  //les parametres du constrcuteur de la classe mere
	
	public List<ParameterDeclaration> getParameterDeclarationList() {
		return parameterDeclarationList;
	}

	public void setParameterDeclarationList(List<ParameterDeclaration> parameterDeclarationList) {
		this.parameterDeclarationList = parameterDeclarationList;
	}

	public List<Expression> getParametres() {
		return parametres;
	}

	public void setParametres(List<Expression> parametres) {
		this.parametres = parametres;
	}

	public Constructor getConstructor() {
		return constructor;
	}

	public SuperClass(List<Expression> parametres) {
		super();
		this.parametres = parametres;
	}
	public SuperClass() {
		super();
	}

	public void setConstructor(Constructor constructor) {
		this.constructor = constructor;
	}


	public ClasseDeclaration getClasseMere() {
		return classeMere;
	}

	public void setClasseMere(ClasseDeclaration classeMere) {
		this.classeMere = classeMere;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean exist = false; // si le constructeur existe de la classe mere
		ClasseDeclaration cd = (ClasseDeclaration) this.instanciation.getDeclaration().getContainer();
		boolean result = true;
		if(cd.getExtension().getInstanciation() != null) {
			for(Definition df : this.classeMere.getDefinitionList()) {
				if(df.getConstructor() != null) {
					exist = true;
				}
			}
			if(!exist) {
				Logger.error("constructor not defined in mother Class !");
				return false;
			}
			exist = false;
			for(Constructor cons : this.classeMere.getConstructorList()) {
				this.parameterDeclarationList = cons.getParameterDeclarationList();
				if(this.parameterDeclarationList != null) {
					if( this.parameterDeclarationList.size() == this.parametres.size()) {
						for(int i = 0; i<this.parametres.size();i++) {
							result = result && this.parametres.get(i).getType().compatibleWith(this.parameterDeclarationList.get(i).getType());
							exist = true;
						}
					}
				}else {
					if(this.parametres.size() == 0) {
						exist = true;
					}
				}

			}

			if(!exist) {
				Logger.error("constructor does not match !!");
				return false;
			}


		}else {
			Logger.error("Class extends does not exist !! ");
			return false;
		}
		return result;


	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.add(_factory.createPush(0));
		return frag;
	}

}
