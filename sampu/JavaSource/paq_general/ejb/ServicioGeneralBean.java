package paq_general.ejb;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_general.ejb.ServicioGeneral;
import paq_sistema.aplicacion.Utilitario;

public @Stateless class ServicioGeneralBean implements ServicioGeneral {
	
	private Utilitario utilitario= new Utilitario();
	
	public String getTipoPersona(String  pmodulo){
		
		String tab_tipo_persona= "select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ( "+pmodulo+")";
		
		 return tab_tipo_persona;
		
	}
	
	public TablaGenerica getTablaTipoPersona(){
		
		TablaGenerica tab_tipo_persona=utilitario.consultar("select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ( 2)");
		 return tab_tipo_persona;
		 
	}
	
	
	

}
