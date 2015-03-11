package paq_presupuesto;

import javax.ejb.EJB;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_programa extends Pantalla {
	
	private Tabla tab_programa=new Tabla();
	private Tabla tab_vigente=new Tabla();
	private Arbol arb_arbol=new Arbol();
	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private Combo com_anio=new Combo();

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	

	public pre_programa(){
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El A�o:"));
		bar_botones.agregarComponente(com_anio);

		tab_programa.setId("tab_programa");
		tab_programa.setHeader("PROGRAMA");
		tab_programa.setTabla("pre_programa", "ide_prpro", 1);
		tab_programa.getColumna("activo_prpro").setValorDefecto("true");
		tab_programa.agregarRelacion(tab_vigente);
		tab_programa.dibujar();
		PanelTabla pat_progama=new PanelTabla();
		pat_progama.setPanelTabla(tab_programa);
		
		///vigente
		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("VIGENTE");
		tab_vigente.setTabla("cont_vigente", "ide_covig", 2);
		tab_vigente.getColumna("activo_covig").setValorDefecto("true");
		tab_vigente.setTipoFormulario(true);
		tab_vigente.getGrid().setColumns(4);
		tab_vigente.dibujar();
		PanelTabla pat_vigente=new PanelTabla();
		pat_vigente.setPanelTabla(tab_vigente);
		
		// ARBOL
				arb_arbol.setId("arb_arbol");
				arb_arbol.setArbol("pre_funcion_programa", "ide_prfup", "codigo_prfup ||' '||detalle_prfup", "pre_ide_prfup");
				//arb_arbol.setCondicion("ide_prfup in (select ide_prfup from cont_vigente where ide_prfup != null and ide_geani=-1 )"); //Carga vacio
				arb_arbol.onSelect("seleccionar_arbol");	
				arb_arbol.dibujar();


				Division div3 = new Division(); //UNE OPCION Y DIV 2
				div3.dividir2(pat_progama, tab_vigente, "50%", "H");
				Division div_division = new Division();
				div_division.setId("div_division");
				div_division.dividir2(arb_arbol, div3, "40%", "V");  //arbol y div3
				agregarComponente(div_division);
				///clasificador
				Boton bot_agregar=new Boton();
				bot_agregar.setValue("Agregar Clasificador");
				bot_agregar.setMetodo("agregarClasificador");
				bar_botones.agregarBoton(bot_agregar);

				set_clasificador.setId("set_clasificador");
				set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
				set_clasificador.setRadio(); //solo selecciona una opcion
				set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1"), "ide_prcla"); 
				set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido(); //pone filtro
				set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();//pone filtro
				set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
				agregarComponente(set_clasificador);


	}
	
	public void seleccionar_arbol(NodeSelectEvent evt) {
		
		arb_arbol.seleccionarNodo(evt);
		tab_programa.setCondicion("ide_prfup="+arb_arbol.getValorSeleccionado());
		tab_programa.ejecutarSql();		
		tab_programa.getColumna("IDE_PRFUP").setValorDefecto(arb_arbol.getValorSeleccionado());
		tab_vigente.ejecutarValorForanea(tab_programa.getValorSeleccionado());
		
		
	}

////clasificador agregar y aceptar

	
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un A�o", "");
			return;
		}
		//Si la tabla esta vacia
		if(tab_programa.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		//Filtrar los clasificadores del a�o seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString()));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
		if(set_clasificador.getValorSeleccionado()!=null){
			tab_programa.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_programa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}



	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_programa.guardar()){
			if(tab_vigente.guardar()){
				guardarPantalla();
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}



	public Tabla getTab_programa() {
		return tab_programa;
	}



	public void setTab_programa(Tabla tab_programa) {
		this.tab_programa = tab_programa;
	}



	public Tabla getTab_vigente() {
		return tab_vigente;
	}



	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}

	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}

	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}
	

}