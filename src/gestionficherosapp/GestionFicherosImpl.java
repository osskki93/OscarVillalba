package gestionficherosapp;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el n�mero de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila m�s
		}

		// dimensionar la matriz contenido seg�n los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzar� una excepci�n
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No se puede escribir en la carpeta: "
					+ arg0 +" porque no tienes permiso");
		}
		//que no exista -> lanzar� una excepci�n
		if (file.exists()) {
			throw new GestionFicherosException("No puedes crear la carpeta " +
						arg0 + " dado que ya existe");
		}
		//crear la carpeta -> lanzar� una excepci�n
		try {
			file.mkdir();
		}
		catch (Exception e) {
				throw new GestionFicherosException("No se puede crear la carpeta");
		}
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzar� una excepci�n
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No se puede escribir en el fichero: "
					+ arg0 +" porque no tienes permiso");
		}
		//que no exista -> lanzar� una excepci�n
		if (file.exists()) {
			throw new GestionFicherosException("No puedes crear el fichero " +
						arg0 + " dado que ya existe");
		}
		//crear el fichero -> lanzar� una excepci�n
		try {
			file.createNewFile();
		}
		catch (Exception e) {
				throw new GestionFicherosException("No se puede crear el fichero");
		}
		actualiza();

	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzar� una excepci�n
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No se puede escribir en la carpeta: "
					+ arg0 +" porque no tienes permiso");
		}
		//que  exista -> lanzar� una excepci�n
		if (!file.exists()) {
			throw new GestionFicherosException("No puedes borrar la carpeta dado que no existe");
		}
		//borrar la carpeta -> lanzar� una excepci�n
		try {
			file.delete();
		}
		catch (Exception e) {
				throw new GestionFicherosException("No se puede borrar la carpeta");
		}
		actualiza();

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignaci�n de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		if (!file.exists()) {
		//Controlar que existe. Si no, se lanzar� una excepci�n
		throw new GestionFicherosException ("Error. " + file.getAbsolutePath() + " doesn't exist"); 
		}
		//Controlar que haya permisos de lectura. Si no, se lanzar� una excepci�n
		if (!file.canRead()) {
			throw new GestionFicherosException ("Error. " + file.getAbsolutePath() + " no se puede leer"); 
		}
		
		//T�tulo
		strBuilder.append("INFORMACI�N DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		if (file.isFile()) {
			strBuilder.append("Fichero");
		}
		else {
			strBuilder.append("Directorio");
		}
		
		//Ubicaci�n
		strBuilder.append("Ubicaci�n: ");
		strBuilder.append(file.getAbsolutePath());
		strBuilder.append("\n");
		
		//Fecha de �ltima modificaci�n
		strBuilder.append("�ltima modificaci�n: ");
		Date d = new Date(file.lastModified());
		Calendar c = new GregorianCalendar(); 
		c.setTime(d);
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		String minuto = Integer.toString(c.get(Calendar.MINUTE));
		String segundo = Integer.toString(c.get(Calendar.SECOND));
		strBuilder.append(dia + "/" + mes + "/" + annio + " " + hora + ":" + minuto + ":" + segundo);
		strBuilder.append("\n");
		
		//Si es un fichero oculto o no
		strBuilder.append("Archivo oculto: ");
		if (file.isHidden()) {
			strBuilder.append("Si");
			strBuilder.append("\n");
		}
		else {
			strBuilder.append("No");
			strBuilder.append("\n");
		}
		
		//Si es un fichero: Tama�o en bytes
		if (file.isFile()) {
			strBuilder.append("Tama�o en bytes: ");
			strBuilder.append(file.length());
			strBuilder.append("\n");
		}
		else {
			strBuilder.append("");
		}
		
		//Si es directorio: N�mero de elementos que contiene, 
		
		if (file.isDirectory()) {
			strBuilder.append("N�mero de elementos que contiene: ");
			strBuilder.append(file.list().length);
			strBuilder.append("\n");
		}
		else {
			strBuilder.append("");
		}
		
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)
		
		if (file.isDirectory()) {
			strBuilder.append("Espacio libre: ");
			strBuilder.append(file.getFreeSpace());
			strBuilder.append("\n");
			strBuilder.append("Espacio disponible: ");
			strBuilder.append(file.getUsableSpace());
			strBuilder.append("\n");
			strBuilder.append("Espacio total: ");
			strBuilder.append(file.getTotalSpace());
			strBuilder.append("\n");
			
		}
		else {
			strBuilder.append("");
		}
		
				
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		File file1 = new File(carpetaDeTrabajo, arg1);
		//que se pueda escribir -> lanzar� una excepci�n
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No se puede escribir: "
					+ arg0 +" porque no tienes permiso");
		}
		//que  exista -> lanzar� una excepci�n
		if (!file.exists()) {
			throw new GestionFicherosException("No existe el archivo");
		}
			
		if (file1.exists()) {
			throw new GestionFicherosException("No puedes darle ese nombre porque ya existe");
		}
		//crear la carpeta -> lanzar� una excepci�n
		try {
			file.renameTo(file1);
		}
		catch (Exception e) {
				throw new GestionFicherosException("No se puede renombrar la carpeta");
		}
		actualiza();
	

	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la direcci�n exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
