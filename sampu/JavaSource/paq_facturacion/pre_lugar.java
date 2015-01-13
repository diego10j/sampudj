package paq_facturacion;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;

public class pre_lugar extends Pantalla{
	private Tabla tab_lugar=new Tabla();
	private Tabla tab_usuario_lugar=new Tabla();
	
	public pre_lugar (){
		tab_lugar.setId("tab_lugar");
		tab_lugar.setTabla("fac_lugar", "ide_falug", 1);
		tab_lugar.agregarRelacion(tab_usuario_lugar);
		tab_lugar.dibujar();
		PanelTabla pat_lugar=new PanelTabla();
		pat_lugar.setPanelTabla(tab_lugar);
		
		tab_usuario_lugar.setId("tab_usuario_lugar");
		tab_usuario_lugar.setTabla("fac_usuario_lugar", "ide_fausl", 2);
		//tab_usuario_lugar.getColumna("ide_usua").setCombo(listaCombo)
		tab_usuario_lugar.dibujar();
		PanelTabla pat_usuario_lugar=new PanelTabla();
		pat_usuario_lugar.setPanelTabla(tab_usuario_lugar);
	//division2
		
		Division div_lugar=new Division();
		div_lugar.dividir2(pat_lugar,pat_usuario_lugar,"50%","H");
		agregarComponente(div_lugar);
		
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_lugar.guardar()) {
			tab_usuario_lugar.guardar();
		}
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		

		
	}


	public Tabla getTab_lugar() {
		return tab_lugar;
	}


	public void setTab_lugar(Tabla tab_lugar) {
		this.tab_lugar = tab_lugar;
	}


	public Tabla getTab_usuario_lugar() {
		return tab_usuario_lugar;
	}


	public void setTab_usuario_lugar(Tabla tab_usuario_lugar) {
		this.tab_usuario_lugar = tab_usuario_lugar;
	}

}