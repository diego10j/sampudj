package paq_presupuesto;



import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeSelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import paq_sistema.aplicacion.Pantalla;

public class pre_contratacion extends Pantalla{


	private Tabla tab_poa=new Tabla();
	private Tabla tab_mes=new Tabla();
	private Tabla tab_reforma=new Tabla();
	private Tabla tab_archivo=new Tabla();
	private Tabla tab_financiamiento=new Tabla();
	private Tabla tab_funcion_programa=new Tabla();

	private Combo com_anio=new Combo();

	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private SeleccionTabla set_funcion=new SeleccionTabla();
	private SeleccionTabla set_actualizarfuncion=new SeleccionTabla();
	private SeleccionTabla set_sub_actividad=new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Arbol arb_arbol = new Arbol();
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();
	private SeleccionTabla sel_poa= new SeleccionTabla();
	private SeleccionTabla sel_resolucion= new SeleccionTabla();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);




	public pre_contratacion(){
		
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setCombo("select ide_geani,detalle_geani from gen_anio order by detalle_geani");
		com_anio.setMetodo("filtrarAnio");
		//com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El A�o:"));
		bar_botones.agregarComponente(com_anio);


		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");



		tab_poa.setId("tab_poa");   
		tab_poa.setHeader("PLAN OPERATIVO ANUAL (POA)");
		tab_poa.setTabla("pre_poa","ide_prpoa",2);
		tab_poa.getColumna("ide_geani").setVisible(false);
		tab_poa.setCondicion("ide_geani=-1");  
		tab_poa.getColumna("objeto_programa_prpoa").setVisible(false);
		tab_poa.getColumna("objetivo_proyecto_prpoa").setVisible(false);
		tab_poa.getColumna("meta_proyecto_prpoa").setVisible(false);
		tab_poa.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_poa.getColumna("ide_coest").setVisible(false);
		tab_poa.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_poa.getColumna("ide_prcla").setAutoCompletar();
		tab_poa.getColumna("ide_prcla").setLectura(true);
		tab_poa.getColumna("ide_prfup").setVisible(false);
		tab_poa.getColumna("ide_prcla").setAncho(25);
		tab_poa.getColumna("activo_prpoa").setLectura(true);
		tab_poa.getColumna("activo_prpoa").setValorDefecto("false");
		tab_poa.getColumna("ide_geare").setCombo("gen_area","ide_geare","detalle_geare","");
		tab_poa.getColumna("presupuesto_inicial_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_poa.getColumna("presupuesto_inicial_prpoa").setValorDefecto("0.00");
		tab_poa.getColumna("presupuesto_inicial_prpoa").setEtiqueta();
		tab_poa.getColumna("reforma_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_poa.getColumna("reforma_prpoa").setValorDefecto("0.00");
		tab_poa.getColumna("reforma_prpoa").setEtiqueta();
		tab_poa.getColumna("presupuesto_codificado_prpoa").setEtiqueta();
		tab_poa.getColumna("presupuesto_codificado_prpoa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_poa.getColumna("presupuesto_codificado_prpoa").setValorDefecto("0.00");
		tab_poa.setTipoFormulario(true);
		tab_poa.getGrid().setColumns(4);

		tab_poa.agregarRelacion(tab_mes);//agraga relacion para los tabuladores
		tab_poa.agregarRelacion(tab_financiamiento);
		tab_poa.agregarRelacion(tab_reforma);


		tab_poa.dibujar();
		PanelTabla pat_poa=new PanelTabla();
		pat_poa.setPanelTabla(tab_poa);

		//EJECUCION MENSUAL
		tab_mes.setId("tab_mes");
		tab_mes.setHeader("EJECUCION MENSUAL (POA)");
		tab_mes.setIdCompleto("tab_tabulador:tab_mes");
		tab_mes.setTabla("pre_poa_mes","ide_prpom",3);
		tab_mes.setCampoForanea("ide_prpoa");
		tab_mes.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_mes.getColumna("activo_prpom").setValorDefecto("true");
		tab_mes.getColumna("activo_prpom").setLectura(true);
		tab_mes.getColumna("valor_presupuesto_prpom").setMetodoChange("actualizarEjecucionMensual");		
		tab_mes.setColumnaSuma("valor_presupuesto_prpom");
		tab_mes.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_mes);

		// REFORMA MENSUAL
		tab_reforma.setId("tab_reforma");
		tab_reforma.setHeader("REFORMA MENSUAL (POA)");
		tab_reforma.setIdCompleto("tab_tabulador:tab_reforma");
		tab_reforma.setTabla("pre_poa_reforma", "ide_prpor",4);
		tab_reforma.setCampoForanea("ide_prpoa");
		tab_reforma.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_reforma.getColumna("ide_gemes").setCombo("select ide_gemes,detalle_gemes from gen_mes order by ide_gemes");
		tab_reforma.getColumna("valor_reformado_prpor").setMetodoChange("valorReforma");
		tab_reforma.getColumna("SALDO_ACTUAL_PRPOR").setLectura(true);
		tab_reforma.getColumna("ide_coest").setVisible(false);
		tab_reforma.getColumna("pre_ide_prpoa").setVisible(false);
		tab_reforma.getColumna("ide_gemes").setVisible(false);
		tab_reforma.getColumna("activo_prpor").setValorDefecto("true");
		tab_reforma.getColumna("activo_prpor").setLectura(true);
		tab_reforma.getColumna("fecha_prpor").setValorDefecto(utilitario.getFechaActual());
		tab_reforma.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);

		//FINANCIAMIENTO
		tab_financiamiento.setId("tab_financiamiento");
		tab_financiamiento.setHeader("FUENTES DE FINANCIAMIENTO (POA)");
		tab_financiamiento.setIdCompleto("tab_tabulador:tab_financiamiento");
		tab_financiamiento.setTabla("pre_poa_financiamiento","ide_prpof",5);
		tab_financiamiento.setCampoForanea("ide_prpoa");
		tab_financiamiento.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_financiamiento.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_financiamiento.getColumna("valor_financiamiento_prpof").setMetodoChange("valorFinanciamiento");
		tab_financiamiento.getColumna("activo_prpof").setValorDefecto("true");
		tab_financiamiento.getColumna("activo_prpof").setLectura(true);
		tab_financiamiento.getColumna("ide_coest").setVisible(false);
		tab_financiamiento.dibujar();
		PanelTabla pat_panel4= new PanelTabla();
		pat_panel4.setPanelTabla(tab_financiamiento);

		//ARCHIVO
		tab_archivo.setId("tab_archivo");
		tab_archivo.setHeader("ARCHIVOS ANEXOS (POA)");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		tab_archivo.setTipoFormulario(true);
		tab_archivo.setTabla("pre_archivo","ide_prarc",6);
		tab_archivo.getColumna("foto_prarc").setUpload("presupuesto");
		tab_archivo.setCampoForanea("ide_prpoa");
		tab_archivo.getColumna("ide_prpac").setVisible(false);
		tab_archivo.getColumna("ide_prcon").setVisible(false);
		tab_archivo.getColumna("ide_prcop").setVisible(false);
		tab_archivo.getColumna("ide_prtra").setVisible(false);
		tab_archivo.getColumna("activo_prarc").setValorDefecto("true");
		tab_archivo.dibujar();
		PanelTabla pat_panel5= new PanelTabla();
		pat_panel5.setPanelTabla(tab_archivo);
		Imagen fondo= new Imagen();   

		fondo.setStyle("text-aling:center;position:absolute;top:100px;left:490px;");
		fondo.setValue("imagenes/logo.png");
		pat_panel5.setWidth("100%");
		pat_panel5.getChildren().add(fondo);

		tab_tabulador.agregarTab("EJECUCION MENSUAL", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA MENSUAL",pat_panel3);
		tab_tabulador.agregarTab("FINANCIAMIENTO", pat_panel4);
		tab_tabulador.agregarTab("ARCHIVOS",pat_panel5);

		// factor_competencia
		arb_arbol.setId("arb_arbol");
		arb_arbol.setArbol("pre_funcion_programa", "ide_prfup", "codigo_prfup ||' '||detalle_prfup", "pre_ide_prfup");
		//arb_arbol.setArbol("CMP_FACTOR_COMPETENCIA", "IDE_CMFAC", "DETALLE_CMFAC", "CMP_IDE_CMFAC");
		arb_arbol.setCondicion("ide_prfup in (select ide_prfup from cont_vigente where ide_prfup != null and ide_geani=-1 )"); //Carga vacio
		arb_arbol.onSelect("seleccionar_arbol");	
		arb_arbol.dibujar();


		Division div3 = new Division(); //UNE OPCION Y DIV 2
		div3.dividir2(pat_poa, tab_tabulador, "50%", "H");
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(arb_arbol, div3, "40%", "V");  //arbol y div3
		agregarComponente(div_division);



		Boton bot_agregar=new Boton();
		bot_agregar.setValue("Agregar Partida Presupuestaria");
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

		
		inicializarSelPoa();
		inicializarSelResolucion();
		

	}
	
	public void actualizarEjecucionMensual(AjaxBehaviorEvent evt) {
		tab_mes.modificar(evt); //Siempre es la primera linea
		tab_mes.sumarColumnas();
		utilitario.addUpdate("tab_tabulador:tab_mes");
 
	}
	//valor Financiamiento
///// para subir vaslores de un tabla a otra 
	public void  calcularValor(){
		double dou_valor_finan=0;
		double dou_valor_codificado=0;
		double dou_valor_refor=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_refor=Double.parseDouble(tab_reforma.getValor("valor_reformado_prpor"));
		} catch (Exception e) {
		}
		
		
		
		String valor_financiamiento=tab_financiamiento.getSumaColumna("valor_financiamiento_prpof")+"";
		dou_valor_finan=Double.parseDouble(valor_financiamiento);
		dou_valor_codificado=dou_valor_finan+dou_valor_refor;

		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_poa.setValor("presupuesto_inicial_prpoa",utilitario.getFormatoNumero(valor_financiamiento,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "presupuesto_inicial_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorFinanciamiento(AjaxBehaviorEvent evt) {
		tab_financiamiento.modificar(evt); //Siempre es la primera linea
		calcularValor();

	}

	/////valor reforma
///// para subir vaslores de un tabla a otra Financiamiento 
	public void  calcularReforma(){
		double dou_valor_refor=0;
		double dou_valor_codificado=0;
		double dou_valor_finan=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_finan=Double.parseDouble(tab_financiamiento.getValor("valor_financiamiento_prpof"));
		} catch (Exception e) {
		}
		
		String valor_reforma=tab_reforma.getSumaColumna("valor_reformado_prpor")+"";
		dou_valor_refor=Double.parseDouble(valor_reforma);
		//calcula valor codficado
		dou_valor_codificado=dou_valor_finan+dou_valor_refor;
		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_poa.setValor("reforma_prpoa",utilitario.getFormatoNumero(valor_reforma,2));
		tab_poa.setValor("presupuesto_codificado_prpoa", utilitario.getFormatoNumero(dou_valor_codificado, 2));
		tab_poa.modificar(tab_poa.getFilaActual());//para que haga el update
		utilitario.addUpdateTabla(tab_poa, "reforma_prpoa,presupuesto_codificado_prpoa", "");	

	}
	
	public void valorReforma(AjaxBehaviorEvent evt) {
		tab_reforma.modificar(evt); //Siempre es la primera linea
		calcularReforma();

	}


	public void seleccionar_arbol(NodeSelectEvent evt) {
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un A�o", "");
			return;
		}
		arb_arbol.seleccionarNodo(evt);
		tab_poa.setCondicion("ide_prfup="+arb_arbol.getValorSeleccionado());
		tab_poa.ejecutarSql();		
		tab_poa.getColumna("IDE_PRFUP").setValorDefecto(arb_arbol.getValorSeleccionado());
		tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		tab_reforma.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		tab_archivo.ejecutarValorForanea(tab_poa.getValorSeleccionado());
		tab_financiamiento.ejecutarValorForanea(tab_poa.getValorSeleccionado());

		
	}

	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un A�o", "");
			return;
		}
		//Si la tabla esta vacia
		if(tab_poa.isEmpty()){
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
			tab_poa.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_poa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}
	
	
	
	////reporte
	//reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Plan Operativo Anual (POA)")){
		TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
		if (rep_reporte.isVisible()){
			
			p_parametros=new HashMap();		
			rep_reporte.cerrar();
			p_parametros.clear();
			aceptarSelPoa();
			
		}
		else if(sel_poa.isVisible()) {
			
			if(sel_poa.getListaSeleccionados().size()>0){	
				p_parametros.put("ide_prpoa",sel_poa.getSeleccionados());
				p_parametros.put("titulo","Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
				p_parametros.put("ide_geani", Integer.parseInt(com_anio.getValue().toString()));
				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				sel_poa.cerrar();
			   self_reporte.dibujar();
			}		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
	
	
	/////REFORMA
	else if(rep_reporte.getReporteSelecionado().equals("Reforma Plan Operativo Anual (POA)")){
		TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani="+com_anio.getValue());
		if (rep_reporte.isVisible()){
			
			p_parametros=new HashMap();		
			rep_reporte.cerrar();
			p_parametros.clear();
			aceptarSelResolucion();
		}
		else if(sel_resolucion.isVisible()) {
			
			if(sel_resolucion.getListaSeleccionados().size()>0){	
				System.out.println("entra reporte");
				p_parametros.put("pnro_resolucion",sel_resolucion.getSeleccionados());
				p_parametros.put("titulo","Reforma Plan Operativo Anual (POA) "+tab_reporte.getValor("detalle_geani"));
				p_parametros.put("ide_geani", Integer.parseInt(com_anio.getValue().toString()));

				self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
				sel_resolucion.cerrar();
			   self_reporte.dibujar();
			}		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
	
}

	public void inicializarSelPoa(){

	/////dialogo para reporte
		sel_poa.setId("sel_poa");
		sel_poa.setSeleccionTabla("select a.ide_prpoa,detalle_programa,programa,detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
				" detalle_subactividad,subactividad,codigo_subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa," +
				" presupuesto_codificado_prpoa,reforma_prpoa,detalle_geani,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_geare" +
				" from pre_poa a" +
				" left join  gen_anio b on a.ide_geani= b.ide_geani" +
				" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" +
				" left join (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad," +
				" detalle_producto,producto,detalle_proyecto,proyecto,detalle_programa ,programa" +
				" from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,detalle_prnfp as subactividad" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3) c, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
				" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
				" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
				" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e" +
				" where a.pre_ide_prfup = b.ide_prfup" +
				" and b.pre_ide_prfup = c.ide_prfup" +
				" and c.pre_ide_prfup = d.ide_prfup" +
				" and d.pre_ide_prfup = e.ide_prfup" +
				" ) f on a.ide_prfup = f.ide_prfup" +
				" left join gen_area g on a.ide_geare=g.ide_geare" +
				" where a.ide_geani=-1" +				
			    " order by codigo_subactividad,a.ide_prpoa", "ide_prpoa");
		sel_poa.getTab_seleccion().ejecutarSql();
		sel_poa.getTab_seleccion().getColumna("detalle_programa").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_proyecto").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_actividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("detalle_subactividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("codigo_subactividad").setFiltro(true);
		sel_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		sel_poa.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_poa);
		
} 

public void aceptarSelPoa(){
	sel_poa.getTab_seleccion().setSql("select a.ide_prpoa,detalle_programa,programa,detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
			" detalle_subactividad,subactividad,codigo_subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa," +
			" presupuesto_codificado_prpoa,reforma_prpoa,detalle_geani,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_geare" +
			" from pre_poa a" +
			" left join  gen_anio b on a.ide_geani= b.ide_geani" +
			" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" +
			" left join (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad," +
			" detalle_producto,producto,detalle_proyecto,proyecto,detalle_programa ,programa" +
			" from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,detalle_prnfp as subactividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3) c, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b" +
			" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e" +
			" where a.pre_ide_prfup = b.ide_prfup" +
			" and b.pre_ide_prfup = c.ide_prfup" +
			" and c.pre_ide_prfup = d.ide_prfup" +
			" and d.pre_ide_prfup = e.ide_prfup" +
			" ) f on a.ide_prfup = f.ide_prfup" +
			" left join gen_area g on a.ide_geare=g.ide_geare" +
			" where a.ide_geani=" +com_anio.getValue()+				
		    " order by codigo_subactividad,a.ide_prpoa");
	sel_poa.getTab_seleccion().ejecutarSql();
	sel_poa.getBot_aceptar().setMetodo("aceptarReporte");
	sel_poa.dibujar();
	
}

public void inicializarSelResolucion (){
/////dialogo para reporte
		sel_resolucion.setId("sel_resolucion");
		sel_resolucion.setSeleccionTabla("select resolucion_prpor as codigo,resolucion_prpor " +
				" from pre_poa_reforma group by resolucion_prpor","codigo");
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_resolucion);
	
}

public void aceptarSelResolucion (){
/////dialogo para reporte
		sel_resolucion.getTab_seleccion().setSql("select resolucion_prpor as codigo,resolucion_prpor " +
				" from pre_poa_reforma group by resolucion_prpor");
		sel_resolucion.getTab_seleccion().ejecutarSql();
		sel_resolucion.getTab_seleccion().getColumna("resolucion_prpor").setFiltro(true);
		sel_resolucion.getBot_aceptar().setMetodo("aceptarReporte");
		sel_resolucion.dibujar();
	
}



	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un A�o");
			return;

		}
		if (tab_poa.isFocus()) {
			tab_poa.insertar();
			tab_poa.setValor("ide_geani", com_anio.getValue()+"");

		}
		else if (tab_mes.isFocus()) {
			tab_mes.insertar();

		}
		else if (tab_reforma.isFocus()) {
			tab_reforma.insertar();
			tab_reforma.setValor("SALDO_ACTUAL_PRPOR", tab_poa.getValor("presupuesto_codificado_prpoa"));

		}

		else if (tab_financiamiento.isFocus()){
			tab_financiamiento.insertar();

		}
		else if (tab_archivo.isFocus()) {
			tab_archivo.insertar();

		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		double presupuesto_inicial = Double.parseDouble(tab_poa.getValor("presupuesto_inicial_prpoa"));
		double presupuesto_codificado = Double.parseDouble(tab_poa.getValor("presupuesto_codificado_prpoa"));
        double presupuesto_ejecutado = tab_mes.getSumaColumna("valor_presupuesto_prpom");
		double presupuesto_reformado = Double.parseDouble(tab_poa.getValor("reforma_prpoa"));
	
        if(presupuesto_inicial<0){
        	utilitario.agregarMensajeInfo("Presupuesto Inicial", "EL valor del presupuesto inicial no puede ser negativo");
            return;
        }
        if(presupuesto_reformado !=0){
        	if(presupuesto_codificado<0){ 
        	utilitario.agregarMensajeInfo("Reforma Mensual", "No puede reforma con un valor superior al presupuesto inicial asignado");
            return;
        	}
        }
        if(presupuesto_ejecutado>presupuesto_codificado){
        	utilitario.agregarMensajeInfo("Ejecuci�n Mensual", "No puede ejecutar un valor superior al presupuesto codificado");
            return;
        }
        
        if (tab_poa.guardar()) {

			if (tab_mes.guardar()) {
				if( tab_financiamiento.guardar()){
					if(tab_reforma.guardar()){
						tab_archivo.guardar();	
						guardarPantalla();
					}

				}
			}
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_poa.isFocus()){
			tab_poa.eliminar();
		}
		else if (tab_mes.isFocus()){
			tab_mes.eliminar();
			tab_mes.sumarColumnas();
			utilitario.addUpdate("tab_mes");


		}
		else if (tab_reforma.isFocus()){
			tab_reforma.eliminar();
			calcularReforma();

		}
		else if (tab_financiamiento.isFocus()){
			tab_financiamiento.eliminar();
		}
		else if (tab_archivo.isFocus()){
			tab_archivo.eliminar();
		}

	}

	public void filtrarAnio(){

		if(com_anio.getValue()!=null){
			arb_arbol.setCondicion("ide_prfup in (select ide_prfup from cont_vigente where not ide_prfup is null and ide_geani="+com_anio.getValue()+")");		
			tab_poa.getColumna("ide_geani").setValorDefecto(com_anio.getValue().toString());
		}
		else{
			arb_arbol.setCondicion("ide_prfup in (select ide_prfup from cont_vigente where not ide_prfup is null and ide_geani=-1 )");
		}
		arb_arbol.ejecutarSql();		
		
		utilitario.addUpdate("arb_arbol");


	}



	public Tabla getTab_poa() {
		return tab_poa;
	}

	public void setTab_poa(Tabla tab_poa) {
		this.tab_poa = tab_poa;
	}

	public Tabla getTab_mes() {
		return tab_mes;
	}

	public void setTab_mes(Tabla tab_mes) {
		this.tab_mes = tab_mes;
	}

	public Tabla getTab_financiamiento() {
		return tab_financiamiento;
	}

	public void setTab_financiamiento(Tabla tab_financiamiento) {
		this.tab_financiamiento = tab_financiamiento;
	}

	public Tabla getTab_archivo() {
		return tab_archivo;
	}

	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}

	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}

	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public Tabla getTab_reforma() {
		return tab_reforma;
	}

	public void setTab_reforma(Tabla tab_reforma) {
		this.tab_reforma = tab_reforma;
	}

	public SeleccionTabla getSet_funcion() {
		return set_funcion;
	}

	public void setSet_funcion(SeleccionTabla set_funcion) {
		this.set_funcion = set_funcion;
	}

	public Tabla getTab_funcion_programa() {
		return tab_funcion_programa;
	}

	public void setTab_funcion_programa(Tabla tab_funcion_programa) {
		this.tab_funcion_programa = tab_funcion_programa;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}
	public SeleccionTabla getSet_sub_actividad() {
		return set_sub_actividad;
	}
	public void setSet_sub_actividad(SeleccionTabla set_sub_actividad) {
		this.set_sub_actividad = set_sub_actividad;
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}
	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}
	public Map getMap_parametros() {
		return map_parametros;
	}
	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}
	public SeleccionTabla getSel_poa() {
		return sel_poa;
	}
	public void setSel_poa(SeleccionTabla sel_poa) {
		this.sel_poa = sel_poa;
	}
	public SeleccionTabla getSel_resolucion() {
		return sel_resolucion;
	}
	public void setSel_resolucion(SeleccionTabla sel_resolucion) {
		this.sel_resolucion = sel_resolucion;
	}




}
