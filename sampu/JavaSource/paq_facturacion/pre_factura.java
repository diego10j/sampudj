package paq_facturacion;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_facturacion.ejb.ServicioFacturacion;
import paq_sistema.aplicacion.Pantalla;
import org.primefaces.event.SelectEvent;
import framework.componentes.AutoCompletar;
import framework.componentes.Etiqueta;


public class pre_factura extends Pantalla{

	private Tabla tab_factura = new Tabla();
	private Tabla tab_detalle_factura=new Tabla();
	private AutoCompletar aut_factura=new AutoCompletar();
	//SELCCION TABLA
	private SeleccionTabla set_pantalla_dias= new SeleccionTabla();
	private SeleccionTabla set_insertarbodega = new SeleccionTabla();
	private SeleccionTabla set_pantallacliente= new SeleccionTabla();
	private SeleccionTabla set_actualizar_cliente = new SeleccionTabla();
	private Confirmar con_guardar_cliente=new Confirmar();



	//CALENDARIO
	private SeleccionCalendario sec_rango_fechas = new SeleccionCalendario();
	private String srt_fecha_inicio;
	private String srt_fecha_fin;
	private double dou_por_iva=0.12;
	double dou_base_no_iva=0;
	double dou_base_cero=0;
	double dou_base_aprobada=0;
	double dou_valor_iva=0;
	double dou_total=0;
	private String valor;
	private List<Fila> lis_fechas_seleccionadas;

	private Dialogo dia_valor=new Dialogo();
	private Texto tex_valor=new Texto();
	String str_smaterial_seleccionado;

	@EJB
	private ServicioFacturacion ser_facturacion = (ServicioFacturacion ) utilitario.instanciarEJB(ServicioFacturacion.class);

	public pre_factura() {

		// TODO Auto-generated constructor stub

		tab_factura.setHeader("FACTURACI�N");
		tab_factura.setId("tab_factura");
		tab_factura.setTabla("fac_factura", "ide_fafac", 1);
		tab_factura.setGenerarPrimaria(false);
		tab_factura.getColumna("ide_fafac").setExterna(true);
		tab_factura.getColumna("ide_fafac").setLectura(true);
		//para q no se dibuje antes q seleccione el autocompletar
		tab_factura.setCondicion("ide_fadaf=-1");
		tab_factura.setTipoFormulario(true);
		tab_factura.getGrid().setColumns(6);
		tab_factura.getColumna("ide_fadaf").setCombo("fac_datos_factura", "ide_fadaf", "serie_factura_fadaf", "");
		tab_factura.getColumna("ide_comov").setVisible(false);
		//tab_factura.getColumna("ide_comov").setCombo("cont_movimiento", "ide_comov", "detalle_asiento_comov", "");
		tab_factura.getColumna("ide_gtemp").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp", "");
		tab_factura.getColumna("ide_sucu").setCombo("sis_sucursal", "ide_sucu", "nom_sucu", "");
		tab_factura.getColumna("ide_retip").setCombo("rec_tipo", "ide_retip", "detalle_retip", "");
		tab_factura.getColumna("ide_tedar").setVisible(false);
		//tab_factura.getColumna("ide_recli").setCombo("select ide_recli,nombre_comercial_recli from rec_clientes order by nombre_comercial_recli");
		tab_factura.getColumna("ide_tetid").setCombo("tes_tipo_documento", "ide_tetid", "detalle_tetid", "");
		tab_factura.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_factura.getColumna("ide_geins").setCombo("gen_institucion", "ide_geins", "detalle_geins", "");


		tab_factura.getColumna("ide_recli").setCombo(ser_facturacion.getClientes());
		tab_factura.getColumna("ide_recli").setAutoCompletar();
		tab_factura.getColumna("ide_recli").setLectura(true);
		
		//TOTALES DE COLOR ROJO--ESTILO DE COLOR ROJO Y NEGRILLA
		tab_factura.getColumna("base_no_iva_fafac").setEtiqueta();
		tab_factura.getColumna("base_no_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");

		tab_factura.getColumna("base_cero_fafac").setEtiqueta();
		tab_factura.getColumna("base_cero_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo

		tab_factura.getColumna("base_aprobada_fafac").setEtiqueta();
		tab_factura.getColumna("base_aprobada_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo

		tab_factura.getColumna("valor_iva_fafac").setEtiqueta();
		tab_factura.getColumna("valor_iva_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo

		tab_factura.getColumna("total_fafac").setEtiqueta();
		tab_factura.getColumna("total_fafac").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_factura.dibujar();
		tab_factura.agregarRelacion(tab_detalle_factura);

		PanelTabla pat_factura=new PanelTabla();
		pat_factura.setPanelTabla(tab_factura);

		// TABLA 2
		tab_detalle_factura.setId("tab_detalle_factura");
		tab_detalle_factura.setTabla("fac_detalle_factura", "ide_fadef", 2);
		tab_detalle_factura.setCampoForanea("ide_fafac");

		// ide_bomat---setcombo y set autocompletar
		tab_detalle_factura.getColumna("ide_bomat").setCombo("select ide_bomat,codigo_bomat,detalle_bomat,iva_bomat from bodt_material order by detalle_bomat");
		tab_detalle_factura.getColumna("ide_bomat").setAutoCompletar();
		//definimos el metodo que va a ejecutar cuando el usuario seleccione del Autocompletar
		tab_detalle_factura.getColumna("ide_bomat").setMetodoChange("seleccionoProducto");


		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_factura.setId("aut_factura");     
		aut_factura.setAutoCompletar("SELECT ide_fadaf,serie_factura_fadaf,autorizacion_fadaf,fecha_impresion_fadaf,fecha_vencimiento_fadaf " +
				"FROM fac_datos_factura WHERE serie_factura_fadaf is not null order by serie_factura_fadaf");
		aut_factura.setMetodoChange("seleccionoAutocompletar"); //ejecuta el metodo seleccionoAutocompletar

		Etiqueta eti_colaborador=new Etiqueta("FACTURACI�N:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_factura);
		bar_botones.agregarBoton(bot_limpiar);


		//DETALLE FACTURA
		tab_detalle_factura.getColumna("total_fadef").setEtiqueta();
		tab_detalle_factura.getColumna("total_fadef").setEstilo("font-size:13px;font-weight:bold;");
		//LLAMAR A ESTE METODO CUANDO EL USUARIO, MODIFIQUE LA CANTIDAD O EL VALOR DESDE LA APLICACION
		tab_detalle_factura.getColumna("cantidad_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.getColumna("valor_fadef").setMetodoChange("calcularDetalle");
		tab_detalle_factura.dibujar();


		PanelTabla pat_detalle_factura= new PanelTabla();
		pat_detalle_factura.setMensajeWarn("DETALLE FACTURACION");
		pat_detalle_factura.setPanelTabla(tab_detalle_factura);


		Division div_division=new Division();
		div_division.dividir2(pat_factura, pat_detalle_factura, "50%", "h");
		agregarComponente(div_division);


		//BOTON FACRURAR PERIODO
		Boton bot_abrir_periodos= new Boton();
		bot_abrir_periodos.setValue("FACTURAR PERIODOS");
		bot_abrir_periodos.setIcon("ui-calendario");
		bot_abrir_periodos.setMetodo("abrirRango");
		bar_botones.agregarBoton(bot_abrir_periodos);
		sec_rango_fechas.setId("sec_rango_fechas");
		sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRango");
		sec_rango_fechas.setFechaActual();



		//---SELECCION TABLA
		set_pantalla_dias.setId("set_pantalla_dias");
		set_pantalla_dias.setTitle("PANTALLA DEL SISTEMA");
		//CONSULTA
		set_pantalla_dias.setSeleccionTabla("select ide_empr,''  as dia, '' as fecha from sis_empresa where ide_empr=-1",  "ide_empr");
		//set_pantalla_dias.setSql("select fecha_ingre as fecha from fac_detalle_factura");
		set_pantalla_dias.getBot_aceptar().setMetodo("aceptarSeleccionFechas");
		//BOTON PARA ABRIR LA TABLA
		Boton bot_abrir_dias= new Boton();
		bot_abrir_dias.setMetodo("abrirSeleccionTabla");
		agregarComponente(set_pantalla_dias);
		agregarComponente(sec_rango_fechas);



		//PANTALLA ESCOGER BODEGA
		set_insertarbodega.setId("set_insertarbodega");
		set_insertarbodega.setTitle("SELECCIONE MATERIAL DE BODEGA");
		set_insertarbodega.setRadio();
		//CONSULTA
		set_insertarbodega.setSeleccionTabla("bodt_material", "ide_bomat", "detalle_bomat,aplica_valor_bomat");
		set_insertarbodega.getBot_aceptar().setMetodo("aceptarBodega");
		set_insertarbodega.getTab_seleccion().getColumna("detalle_bomat").setNombreVisual("MATERIAL");
		set_insertarbodega.getTab_seleccion().getColumna("aplica_valor_bomat").setVisible(false);//Oculta campo
		//set_insertarBodega.getTab_seleccion().getColumna("valor_bomat").setVisible(false);//Oculta campo
		agregarComponente(set_insertarbodega);


		//PANTALLA TEXT0
		dia_valor.setId("dia_valor");
		dia_valor.setTitle("INGRESE VALOR DEL MATERIAL");
		dia_valor.getBot_aceptar().setMetodo("AceptarValor");
		dia_valor.setWidth("25%");
		dia_valor.setHeight("18%");

		Grid gri_valor=new Grid();
		gri_valor.setColumns(2);
		gri_valor.getChildren().add(new Etiqueta("Valor Aplica: "));
		tex_valor.setSoloNumeros();
		gri_valor.getChildren().add(tex_valor);

		dia_valor.setDialogo(gri_valor);		
		agregarComponente(dia_valor);

		// PARA Q SALGA EN UNA SOLA PAGINA
		set_pantalla_dias.getTab_seleccion().setRows(200);

		//Boton Actualizar Cliente
		Boton bot_actualizar_cliente=new Boton();
		bot_actualizar_cliente.setIcon("ui-icon-person");
		bot_actualizar_cliente.setValue("ACTUALIZAR CLIENTE");
		bot_actualizar_cliente.setMetodo("actualizarCliente");
		bar_botones.agregarBoton(bot_actualizar_cliente);

		con_guardar_cliente.setId("con_guardar_cliente");
		agregarComponente(con_guardar_cliente);

		set_actualizar_cliente.setId("set_actualizar_cliente");
		set_actualizar_cliente.setSeleccionTabla(ser_facturacion.getClientes(),"ide_recli");
		set_actualizar_cliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_actualizar_cliente.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_actualizar_cliente.setRadio();
		set_actualizar_cliente.getBot_aceptar().setMetodo("modificarCliente");
		agregarComponente(set_actualizar_cliente);
		


		//BOTON AGREGAR CLIENTE
		Boton bot_agregarCliente=new Boton();
		bot_agregarCliente.setValue("AGREGAR CLIENTE");
		bot_agregarCliente.setIcon("ui-icon-person");
		bot_agregarCliente.setMetodo("agregarCliente");
		bar_botones.agregarBoton(bot_agregarCliente);

		//PANTALLA SELECIONAR CLIENTE
		set_pantallacliente.setId("set_pantallacliente");
		set_pantallacliente.setTitle("SELECCIONE CLIENTES");
		set_pantallacliente.getBot_aceptar().setMetodo("aceptarCliente");
		set_pantallacliente.setSeleccionTabla(ser_facturacion.getClientes(),"ide_recli");
		set_pantallacliente.getTab_seleccion().getColumna("ruc_comercial_recli").setFiltro(true);
		set_pantallacliente.getTab_seleccion().getColumna("nombre_comercial_recli").setFiltro(true);
		set_pantallacliente.setRadio();
		set_pantallacliente.getTab_seleccion().ejecutarSql();
		agregarComponente(set_pantallacliente);
		

	}


	public void agregarCliente(){
		utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		//Hace aparecer el componente
		if(aut_factura.getValor()!=null){
			set_pantallacliente.getTab_seleccion().setSql(ser_facturacion.getClientes());
			set_pantallacliente.getTab_seleccion().ejecutarSql();
			set_pantallacliente.dibujar();
		}	
	}

	public void aceptarCliente(){
		String str_seleccionado=set_pantallacliente.getValorSeleccionado();
		if(str_seleccionado!=null){
			//Inserto los cleintes seleccionados en la tabla  
			if(tab_factura.isFilaInsertada()==false){
				//Controla que si ya esta insertada no vuelva a insertar
				tab_factura.insertar();	
			}
			
			tab_factura.setValor("ide_recli", str_seleccionado);				

			set_pantallacliente.cerrar();
			utilitario.addUpdate("tab_factura");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
	
	//ACTUALIZAR CLIENTE
		public void actualizarCliente(){
			if (tab_factura.getValor("ide_recli")==null){
				utilitario.agregarMensajeInfo("Debe seleccionar un cliente para actualizar","");
				return;
			}
			set_actualizar_cliente.getTab_seleccion().setSql(ser_facturacion.getClientes());
			set_actualizar_cliente.getTab_seleccion().ejecutarSql();
			set_actualizar_cliente.dibujar();
		}

		public void modificarCliente(){
			String str_clienteActualizado=set_actualizar_cliente.getValorSeleccionado();
			if(str_clienteActualizado!=null){
		
			tab_factura.modificar(tab_factura.getFilaActual());
			tab_factura.setValor("ide_recli", str_clienteActualizado);
			utilitario.addUpdate("tab_factura");
			
			con_guardar_cliente.setMessage("Esta Seguro de Actualizar el Cliente");
			con_guardar_cliente.setTitle("CONFIRMCION DE ACTUALIZAR");
			con_guardar_cliente.getBot_aceptar().setMetodo("guardarActualizarCliente");
			con_guardar_cliente.dibujar();
			utilitario.addUpdate("con_guardar_cliente");
			}

		}

		public void guardarActualizarCliente(){
			tab_factura.guardar();
			con_guardar_cliente.cerrar();
			set_actualizar_cliente.cerrar();

			guardarPantalla();
		}
	
	private void insertarMaterial(String ide_bomat,String valor){
		//Inserta en la tabla de detalles el matiial seleccionado
		for (int j = 0; j < lis_fechas_seleccionadas.size(); j++) {
			Object[] fila =lis_fechas_seleccionadas.get(j).getCampos();
			//Obtenemos el campo de la fecha seleccionada
			String str_fecha_actual =fila[2]+"";
			tab_detalle_factura.insertar(); //inserto
			tab_detalle_factura.setValor("ide_bomat",ide_bomat);//asigno material
			tab_detalle_factura.setValor("fecha_fadef",str_fecha_actual);//asig fecha
			tab_detalle_factura.setValor("observacion_fadef",
					tab_detalle_factura.getValorArreglo("ide_bomat",2)+" "+str_fecha_actual);
			tab_detalle_factura.setValor("valor_fadef",valor);
			tab_detalle_factura.setValor("total_fadef",valor);

		}
		calcularFactura();//calcula los totales
	}


	public void aceptarBodega(){
		String str_seleccionado=set_insertarbodega.getValorSeleccionado();//x q es radio
		TablaGenerica validarTarifaUnica=utilitario.consultar("select ide_recli, aplica_mtarifa_recli from rec_clientes where aplica_mtarifa_recli=true and ide_recli="+tab_factura.getValor("ide_recli"));
		TablaGenerica retornarValorUnico= utilitario.consultar("select a.ide_recli, a.aplica_mtarifa_recli, valor_temat from rec_clientes a, tes_tarifas b, tes_material_tarifa c where a.aplica_mtarifa_recli=true and " +
				"b.ide_tetar= c.ide_tetar  and a.ide_recli="+tab_factura.getValor("ide_recli")+" and ide_bomat="+set_insertarbodega.getValorSeleccionado());
		TablaGenerica retornaValorMultiple=utilitario.consultar("select ide_teclt,a.ide_recli,ide_bomat,valor_temat from tes_cliente_tarifa a, rec_clientes b,tes_material_tarifa c"+
				" where a.ide_recli = b.ide_recli and a.ide_temat = c.ide_temat and a.ide_recli ="+tab_factura.getValor("ide_recli")+" and ide_bomat="+set_insertarbodega.getValorSeleccionado());
		
		if(validarTarifaUnica!=null){
			valor=retornaValorMultiple.getValor("valor_temat");
			System.out.println("Multiple"+retornaValorMultiple.getSql());
			
		}
		else if(validarTarifaUnica==null){
			valor=retornarValorUnico.getValor("valor_temat");
			System.out.println("Valor Unico"+retornarValorUnico.getSql());
		}
		
		if(str_seleccionado!=null){// valido que seleccione

			for (int j = 0; j < lis_fechas_seleccionadas.size(); j++) {
				Object[] fila =lis_fechas_seleccionadas.get(j).getCampos();
				//Obtenemos el campo de la fecha seleccionada
				String str_fecha_actual =fila[2]+"";

				tab_detalle_factura.insertar(); //inserto
				tab_detalle_factura.setValor("ide_bomat",str_seleccionado);//asigno material
				tab_detalle_factura.setValor("fecha_fadef",str_fecha_actual);//asig material
				tab_detalle_factura.setValor("observacion_fadef",
						tab_detalle_factura.getValorArreglo("ide_bomat", 2)+""+str_fecha_actual);
				tab_detalle_factura.setValor("cantidad_fadef", "1");

			}
			set_insertarbodega.cerrar();
			utilitario.addUpdate("tab_detalle_factura");
			utilitario.addUpdate("tab_factura");
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar un material", "");
		}
	}



	public void AceptarValor(){
		if(tex_valor.getValue()!=null){
			String str_valor=tex_valor.getValue()+"";
			insertarMaterial(str_smaterial_seleccionado,str_valor+"");
			dia_valor.cerrar();
			utilitario.addUpdate("tab_detalle_factura");
			utilitario.addUpdate("tab_factura");
		}
		else{
			utilitario.agregarMensajeError("Debe Ingresar un valor", "");	
		}

	}

	public void aceptarSeleccionFechas(){
		String str_seleccionados=set_pantalla_dias.getSeleccionados();
		if(str_seleccionados!=null){ //valida que seleccione almenos 1 dia
			utilitario.agregarMensaje("Buscar dias",set_pantalla_dias.getSeleccionados()+"");
			//Fechas que selecciono
			lis_fechas_seleccionadas=set_pantalla_dias.getListaSeleccionados(); // seleccionar filas
			set_pantalla_dias.cerrar(); //cierro seleccion dias
			set_insertarbodega.getTab_seleccion().ejecutarSql();
			set_insertarbodega.dibujar(); //abro seleccion bodega
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar almenos un día", "");
		}
	}

	public void abrirRango(){
		utilitario.agregarMensaje("Requiere ingresar una factura para ingresar los detalles", "");
		//Hace aparecer el componente
		if(aut_factura.getValor()!=null){
			sec_rango_fechas.dibujar();
		}
	}


	public void aceptarRango(){
		//Si las fechas seleccionadas son validas, muestra las fechas seleccionadas
		if(sec_rango_fechas.isFechasValidas()){
			//Almacenamos las fechas seleccionadas en variables
			srt_fecha_inicio=sec_rango_fechas.getFecha1String();
			srt_fecha_fin=sec_rango_fechas.getFecha2String();

			//Valiada que maxiomo seleccione un mes
			int int_num_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(srt_fecha_inicio),utilitario.getFecha(srt_fecha_fin));
			if(int_num_dias<=31){
				//Cerramos el seleccionCalendario
				sec_rango_fechas.cerrar();       

				//Abrimos el seleccionTabla
				set_pantalla_dias.setDynamic(false);
				set_pantalla_dias.dibujar();
				insertarDias();  // llenamos la tabla
			}
			else{
				utilitario.agregarMensajeError("El rango debe ser m�ximo de un mes(31 d�as)", " Seleccione un rango menor");
			}

		}

		else{
			utilitario.agregarMensajeError("Las fecha seleccionadas no son v�lidas", "");
		}
	}

	public void insertarDias(){
		//limpiamos el seleccion tabla
		set_pantalla_dias.getTab_seleccion().limpiar();
		set_pantalla_dias.getTab_seleccion().setLectura(false);//para qpermita insertar
		//Obtenemos el numero de dias entre las dos fechas
		int int_num_dias=utilitario.getDiferenciasDeFechas(utilitario.getFecha(srt_fecha_inicio), utilitario.getFecha(srt_fecha_fin));
		//System.out.println("NUM DIAS: "+int_num_dias);
		//Insertamos el rango de dias
		Date dat_fecha_actual=utilitario.getFecha(srt_fecha_inicio);//fecha q vamos a restar los dias
		for(int i=0;i<=int_num_dias;i++){
			//insertamos en la tabla seleccion
			set_pantalla_dias.getTab_seleccion().insertar();
			set_pantalla_dias.getTab_seleccion().getFilaSeleccionada().setRowKey((i+1)+"");
			//asignamos valores a los capos insertados
			set_pantalla_dias.getTab_seleccion().setValor("ide_empr", i+"");
			//set_pantalla_dias.getTab_seleccion().setValor("dia",utilitario.getFormatoFecha(dat_fecha_actual));
			//FECHA LARGA
			set_pantalla_dias.getTab_seleccion().setValor("dia",utilitario.getFechaLarga(utilitario.getFormatoFecha(dat_fecha_actual)));
			//FECHA
			set_pantalla_dias.getTab_seleccion().setValor("fecha",utilitario.getFormatoFecha(dat_fecha_actual));

			//resto un dia a la fecha
			dat_fecha_actual=utilitario.sumarDiasFecha(dat_fecha_actual,1 );
		}
	}

	public void limpiar(){
		aut_factura.limpiar();
		tab_factura.limpiar();
		tab_detalle_factura.limpiar();
		utilitario.addUpdate("aut_factura");
	}


	//METDO AUTOCOMPLETAR
	public void seleccionoAutocompletar(SelectEvent evt){
		//Cuando selecciona una opcion del autocompletar siempre debe hacerse el onSelect(evt)
		aut_factura.onSelect(evt);
		tab_factura.setCondicion("ide_fadaf="+aut_factura.getValor());
		tab_factura.ejecutarSql();
		//tab_factura.ejecutarValorForanea(val)
		tab_detalle_factura.ejecutarValorForanea(tab_factura.getValorSeleccionado());

	}

	// metodo tieneIvaProducto
	private  boolean tieneIvaProducto(String ide_bodtmat){
		//Declaramos un String con la consulta que vamos a ejecutar
		String str_sql="Select * from bodt_material where ide_bomat="+ide_bodtmat;
		//Asi se hacen consultas a la BDD
		TablaGenerica tab_consulta=utilitario.consultar(str_sql);

		//Preguntamos si la tabla no esta vacia es decir que si retorno un resultado la consulta
		if ( tab_consulta.isEmpty()==false) {
			//Obtenemos el valor del campo y lo almacenamos en un String
			String str_aplica_valor_bomat= tab_consulta.getValor("aplica_valor_bomat");
			//Preguntamos si el valor de la variable es true
			if(str_aplica_valor_bomat!=null && str_aplica_valor_bomat.equalsIgnoreCase("true")){
				return true; //Si carga iva
			}
		}
		//System.out.println(tab_consulta.getValor("aplica_valor_bomat"));
		return false;  //retorna false
	}

	//Metodo metodo cuando se seleccione algun producto del autocompletar
	public void seleccionoProducto(SelectEvent evt){
		tab_detalle_factura.modificar(evt); //simepre que se ejecuta un metodoChange
		//Consultamos si el producto seleccionado carga iva
		boolean boo_iva=tieneIvaProducto(tab_detalle_factura.getValor("ide_bomat"));
		//Mensaje producto, carga o no garga iva
		utilitario.agregarMensaje(tab_detalle_factura.getValor("ide_bomat"),boo_iva+"");
	}

	//total_fadef
	public void calcular(){
		//Variables para almacenar y calcular el total del detalle
		double dou_cantidad_fadef=0;
		double dou_valor_fadef=0;
		double dou_total_fadef=0;

		try {
			//Obtenemos el valor de la cantidad
			dou_cantidad_fadef=Double.parseDouble(tab_detalle_factura.getValor("cantidad_fadef"));
		} catch (Exception e) {
		}

		try {
			//Obtenemos el valor
			dou_valor_fadef=Double.parseDouble(tab_detalle_factura.getValor("valor_fadef"));
		} catch (Exception e) {
		}

		//Calculamos el total
		dou_total_fadef=dou_cantidad_fadef*dou_valor_fadef;

		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_detalle_factura.setValor("total_fadef",utilitario.getFormatoNumero(dou_total_fadef,3));

		//Actualizamos el campo de la tabla AJAX
		utilitario.addUpdateTabla(tab_detalle_factura, "total_fadef", "tab_factura");
		calcularFactura();
	}

	public void calcularDetalle(AjaxBehaviorEvent evt) {
		tab_detalle_factura.modificar(evt); //Siempre es la primera linea
		calcular();
	}

	//Vamos a calcular los totales tanto de iva como de valores de toda la factura

	private void calcularFactura(){
		//Enceramos las variables
		dou_base_no_iva=0;
		dou_base_cero=0;
		dou_base_aprobada=0;
		dou_valor_iva=0;
		dou_total=0;

		//Reccoremos todos los detalles de la factura
		for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
			//Obtenemos si el producto actual tiene iva
			boolean boo_iva=tieneIvaProducto(tab_detalle_factura.getValor(i,"ide_bomat"));
			if(boo_iva){
				//CARGA IVA
				try {
					//Acumulamos la base aprobada
					dou_base_aprobada+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
					//Acumulamos el valor del iva de cada detalle
					dou_valor_iva+=(Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef")) *  dou_por_iva);
				} catch (Exception e) {
				}
			}
			else{
				//NO CARGA IVA
				try {
					//Acumulamos la base no iva
					dou_base_no_iva+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
					//Acumulamos la base cero
					dou_base_cero+=Double.parseDouble(tab_detalle_factura.getValor(i,
							"total_fadef"));
				} catch (Exception e) {
				}
			}
		}
		dou_total=dou_valor_iva+dou_base_aprobada+dou_base_no_iva;
		tab_factura.setValor("base_no_iva_fafac",utilitario.getFormatoNumero(dou_base_no_iva,3));
		tab_factura.setValor("base_cero_fafac",utilitario.getFormatoNumero(dou_base_cero,3));
		tab_factura.setValor("base_aprobada_fafac",utilitario.getFormatoNumero(dou_base_aprobada,3));
		tab_factura.setValor("valor_iva_fafac",utilitario.getFormatoNumero(dou_valor_iva,3));
		tab_factura.setValor("total_fafac",utilitario.getFormatoNumero(dou_total,3));
		tab_factura.modificar(tab_factura.getFilaActual());//para que haga el update
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (aut_factura.getValor()!=null){
			if(tab_factura.isFocus()){
				tab_factura.getColumna("ide_fadaf").setValorDefecto(aut_factura.getValor());
				tab_factura.insertar();
			}
			else if(tab_detalle_factura.isFocus()){
				tab_detalle_factura.insertar();
			}
		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar los datos de FacturaciÃƒÂƒÃ‚Â³n","");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_factura.guardar()){
			if(tab_detalle_factura.guardar()){
				guardarPantalla();
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		if(tab_detalle_factura.isFocus()){
			calcularFactura();//calcula los totales
			utilitario.addUpdate("tab_factura"); // actualiza la tabla
			if(tab_factura.isFilaModificada()){
				//Para que haga el update
				tab_factura.modificar(tab_factura.getFilaActual());
			}
		}

	}



	public Tabla gettab_factura() {
		return tab_factura;
	}

	public void settab_factura(Tabla tab_factura) {
		this.tab_factura = tab_factura;
	}

	public Tabla gettab_detalle_factura() {
		return tab_detalle_factura;
	}

	public void settab_detalle_factura(Tabla tab_detalle_factura) {
		this.tab_detalle_factura = tab_detalle_factura;
	}


	public AutoCompletar getAut_factura() {
		return aut_factura;
	}

	public void setAut_factura(AutoCompletar aut_factura) {
		this.aut_factura = aut_factura;
	}


	public SeleccionCalendario getSec_rango_fechas() {
		return sec_rango_fechas;
	}


	public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
		this.sec_rango_fechas = sec_rango_fechas;
	}

	public SeleccionTabla getSet_pantalla_dias() {
		return set_pantalla_dias;
	}

	public void setSet_pantalla_dias(SeleccionTabla set_pantalla_dias) {
		this.set_pantalla_dias = set_pantalla_dias;
	}


	public Dialogo getDia_valor() {
		return dia_valor;
	}


	public void setDia_valor(Dialogo dia_valor) {
		this.dia_valor = dia_valor;
	}



	public SeleccionTabla getSet_insertarbodega() {
		return set_insertarbodega;
	}


	public void setSet_insertarbodega(SeleccionTabla set_insertarbodega) {
		this.set_insertarbodega = set_insertarbodega;
	}


	public SeleccionTabla getSet_pantallacliente() {
		return set_pantallacliente;
	}


	public void setSet_pantallacliente(SeleccionTabla set_pantallacliente) {
		this.set_pantallacliente = set_pantallacliente;
	}


	public SeleccionTabla getSet_actualizar_cliente() {
		return set_actualizar_cliente;
	}


	public void setSet_actualizar_cliente(SeleccionTabla set_actualizar_cliente) {
		this.set_actualizar_cliente = set_actualizar_cliente;
	}


	public Confirmar getCon_guardar_cliente() {
		return con_guardar_cliente;
	}


	public void setCon_guardar_cliente(Confirmar con_guardar_cliente) {
		this.con_guardar_cliente = con_guardar_cliente;
	}



}
