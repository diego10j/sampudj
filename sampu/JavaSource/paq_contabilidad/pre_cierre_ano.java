package paq_contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_cierre_ano extends Pantalla{

	private Tabla tab_cierre_a�o=new Tabla();
	
	
	
	public pre_cierre_ano() {
		tab_cierre_a�o.setId("tab_cierre_a�o");  
		tab_cierre_a�o.setTabla("cont_cierre_ano","ide_cocia", 1);	
	    tab_cierre_a�o.getColumna("ide_cocim").setCombo("cont_cierre_movimiento", "ide_cocim", "detalle_cocim", "");
        tab_cierre_a�o.getColumna("ide_geani").setCombo("gen_anio", "ide_geani", "detalle_geani", "");
		tab_cierre_a�o.dibujar();
		PanelTabla pat_cierre_a�o=new PanelTabla();
		pat_cierre_a�o.setPanelTabla(tab_cierre_a�o);
		
		agregarComponente(pat_cierre_a�o);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_cierre_a�o.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_cierre_a�o.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_cierre_a�o.eliminar();
	}

	public Tabla gettab_cierre_a�o() {
		return tab_cierre_a�o;
	}

	public void settab_cierre_a�o(Tabla tab_cierre_a�o) {
		this.tab_cierre_a�o = tab_cierre_a�o;
	}


}
