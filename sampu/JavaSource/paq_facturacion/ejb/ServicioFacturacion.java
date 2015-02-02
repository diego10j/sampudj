package paq_facturacion.ejb;

import javax.ejb.Stateless;

import paq_sistema.aplicacion.Utilitario;

import framework.aplicacion.TablaGenerica;


/**
 * Session Bean implementation class ServicioFacturacion
 */
@Stateless

public class ServicioFacturacion {
	private Utilitario utilitario=new Utilitario();


	public String getClientes(String matrizSucursal ){
	    String tab_cliente="select a.ide_recli, ruc_comercial_recli,nombre_comercial_recli," +
	            "  nro_establecimiento_recli, codigo_zona_recli, " +
	            " telefono_factura_recli, direccion_recld from rec_clientes a " +
	            " LEFT JOIN rec_cliente_direccion b on a.ide_recli=b.ide_recli where MATRIZ_SUCURSAL_RECLI in ("+matrizSucursal+")" +
	                " ORDER BY  nombre_comercial_recli";
	    return tab_cliente;
	}
	
	public TablaGenerica getTablaBodega (String tabla){
		
		TablaGenerica tab_bodega=utilitario.consultar("select a.ide_recli, a.aplica_mtarifa_recli " +
				"FROM rec_clientes a, tes_tarifas b " +
				"WHERE a.ide_tetar=b.ide_tetar and a.aplica_mtarifa_recli=true and ide_recli=1 and b.ide_tetar=1"+tabla+"'");
		return tab_bodega;
		
		}
	
	public String getFacturas(String numeroFactura ){
	    String tab_cliente="SELECT ide_bomat, codigo_bomat, detalle_bomat FROM bodt_material b WHERE codigo_bomat is not null " +
				"and ide_bogrm in(select c.ide_bogrm from fac_usuario_lugar  a " +
				"inner join fac_lugar b on a.ide_falug=b.ide_falug and a.ide_usua=" +utilitario.getVariable("ide_usua")+
				" inner join fac_venta_lugar c on c.ide_falug=b.ide_falug " +
				"inner join  bodt_grupo_material d on c.ide_bogrm=d.ide_bogrm ) order by detalle_bomat";
	    return tab_cliente;
	}


	public ServicioFacturacion() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Este servicio retorna los datos de la factura
	 * @param grupos recibe 1 para mostrar todos los materiales y
	 * @param grupoMaterial recibe 0 para mostrar solo por grupos de acuerdo los ide enviados en el parámetro 
	 * @return retorna un string con el siguiente contenido (codigo datos factura, autorización 
	 */
	public String getDatosFactura(String grupos,String grupoMaterial){
		String tab_datos_factura="select ide_fadaf,autorizacion_sri_bogrm,serie_factura_fadaf, detalle_bogrm " +
						" from fac_datos_factura a, bodt_grupo_material b where a.ide_bogrm = b.ide_bogrm ";
						if(grupos.equals("0")){
							tab_datos_factura +=" and b.ide_bogrm in ("+grupoMaterial+") ";
						}
						tab_datos_factura += " order by autorizacion_sri_bogrm";
						System.out.println("datos factura");
						return tab_datos_factura;
	}
}
