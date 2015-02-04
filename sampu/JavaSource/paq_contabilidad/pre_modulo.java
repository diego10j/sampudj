package paq_contabilidad;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_sistema.aplicacion.Pantalla;

public class pre_modulo extends Pantalla{

	private Tabla tab_modulo=new Tabla();
	private Tabla tab_modulo_estado=new Tabla();
	private Tabla tab_persona_modulo=new Tabla();
	private Tabla tab_modulo_secuencial=new Tabla();
	private Tabla tab_parametro=new Tabla();
	private Arbol arb_modulo = new Arbol();
	private Division div_modulo =new Division();
	private Division div_division=new Division();
	
	
	
	
	
	
	
	public pre_modulo() {
		tab_modulo.setId("tab_modulo");  
		tab_modulo.setTabla("gen_modulo","ide_gemod", 1);	
		tab_modulo.agregarRelacion(tab_modulo_estado);
		tab_modulo.agregarRelacion(tab_persona_modulo);
		tab_modulo.agregarRelacion(tab_modulo_secuencial);
		tab_modulo.agregarRelacion(tab_parametro);
		tab_modulo.agregarArbol(arb_modulo);
		tab_modulo.dibujar();
		PanelTabla pat_modulo=new PanelTabla();
		pat_modulo.setPanelTabla(tab_modulo);
		
		//agregarComponente(pat_modulo);
		Tabulador tab_Tabulador=new Tabulador();
		tab_Tabulador.setId("tab_tabulador");
		
				
		
		//Estado por modulo//
		tab_modulo_estado.setId("tab_modulo_estado");
		tab_modulo_estado.setIdCompleto("tab_tabulador:tab_modulo_estado");
		tab_modulo_estado.setTabla("gen_modulo_estado","ide_gemoe", 2);
		tab_modulo_estado.getColumna("ide_gemod").setCombo("gen_modulo", "ide_gemod", "detalle_gemod", "");
		tab_modulo_estado.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_modulo_estado.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_modulo_estado);
		
		//persona por modulo
		
		tab_persona_modulo.setId("tab_persona_modulo");
		tab_persona_modulo.setIdCompleto("tab_tabulador:tab_persona_modulo");
		tab_persona_modulo.setTabla("gen_tipo_persona_modulo", "ide_getpm", 3);
		tab_persona_modulo.getColumna("ide_getip").setCombo("gen_tipo_persona", "ide_getip", "detalle_getip", "");
		tab_persona_modulo.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_persona_modulo);
		
		//modulo secuencial
		tab_modulo_secuencial.setId("tab_modulo_secuencial");
		tab_modulo_secuencial.setIdCompleto("tab_tabulador:tab_modulo_secuencial");
		tab_modulo_secuencial.setTabla("gen_modulo_secuencial", "ide_gemos", 4);
		tab_modulo_secuencial.getColumna("ide_gemod").setCombo("gen_modulo", "ide_gemod", "detalle_gemod", "");
		tab_modulo_secuencial.dibujar();
		PanelTabla pat_panel4= new PanelTabla();
		pat_panel4.setPanelTabla(tab_modulo_secuencial);
		
		//pagametros modulo
		tab_parametro.setId("tab_parametro");
		tab_parametro.setIdCompleto("tab_tabulador:tab_parametro");
		tab_parametro.setTabla("cont_parametro_modulo", "ide_copam", 5);
		tab_parametro.getColumna("ide_copag").setCombo("cont_parametros_general", "ide_copag", "detalle_copag", "");
		tab_parametro.dibujar();
		PanelTabla pat_panel5=new PanelTabla();
		pat_panel5.setPanelTabla(tab_parametro);
		
		tab_Tabulador.agregarTab("ESTADO POR MODULOS", pat_panel2);
		tab_Tabulador.agregarTab("PERSONA POR MODULOS", pat_panel3);
		tab_Tabulador.agregarTab("MODULOS SECUENCIAL", pat_panel4);
		tab_Tabulador.agregarTab("PARAMETRO MODULOS", pat_panel5);
		
	
		
		
		//dividir2
		div_division=new Division();
		div_division.dividir2(pat_modulo,tab_Tabulador,"50%","h");
		//agregarComponente(div_division);
		// division arbol
		//arbol
		arb_modulo.setId("arb_modulo");
		arb_modulo.setArbol("gen_modulo", "ide_gemod", "detalle_gemod", "gen_ide_gemod");
		arb_modulo.dibujar();
		div_modulo =new Division();
		div_modulo.setId("div_modulo");
		div_modulo.dividir2(arb_modulo, div_division, "25%", "v");
		agregarComponente(div_modulo);
		
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_modulo.isFocus()){
			tab_modulo.insertar();
		}
				
				
		
			else if (tab_modulo_estado.isFocus()) {
				tab_modulo_estado.insertar();
			
			
				}
			else if (tab_persona_modulo.isFocus()) {
				tab_persona_modulo.insertar();
				
				}
			else if (tab_modulo_secuencial.isFocus()) {
				tab_modulo_secuencial.insertar();
				
			}
			else if (tab_parametro.isFocus()) {
				tab_parametro.insertar();
				
			}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_modulo.guardar()){
			if (tab_modulo_estado.guardar()) {
				if (tab_persona_modulo.guardar()) {
					if (tab_modulo_secuencial.guardar()){
						tab_parametro.guardar();
					}
				}
				
				
			}
		}
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	
	}

	public Tabla gettab_modulo() {
		return tab_modulo;
	}

	public void settab_modulo(Tabla tab_modulo) {
		this.tab_modulo = tab_modulo;
	}

	public Tabla getTab_modulo_estado() {
		return tab_modulo_estado;
	}

	public void setTab_modulo_estado(Tabla tab_modulo_estado) {
		this.tab_modulo_estado = tab_modulo_estado;
	}

	public Tabla getTab_persona_modulo() {
		return tab_persona_modulo;
	}

	public void setTab_persona_modulo(Tabla tab_persona_modulo) {
		this.tab_persona_modulo = tab_persona_modulo;
	}

	public Arbol getArb_modulo() {
		return arb_modulo;
	}

	public void setArb_modulo(Arbol arb_modulo) {
		this.arb_modulo = arb_modulo;
	}

	public Tabla getTab_modulo_secuencial() {
		return tab_modulo_secuencial;
	}

	public void setTab_modulo_secuencial(Tabla tab_modulo_secuencial) {
		this.tab_modulo_secuencial = tab_modulo_secuencial;
	}

	public Tabla getTab_parametro() {
		return tab_parametro;
	}

	public void setTab_parametro(Tabla tab_parametro) {
		this.tab_parametro = tab_parametro;
	}


}
