drop database if exists DBKinalExpress;
Create database DBKinalExpress;
use DBKinalExpress;

create table Clientes(
	codigoCliente int not null,
    NITCliente varchar(12) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150),
    telefonoCliente varchar(12),
    correoCliente varchar(45),
    primary key PK_Clientes(codigoCliente)
);

create table CargoEmpleado(
	codigoCargoEmpleado int not null,
    nombreCargo varchar(45) not null,
    descripcionCargo varchar(60) not null,
    primary key PK_codigoCargoEmpleado(codigoCargoEmpleado)
);

create table Empleados(
	codigoEmpleado int not null,
    nombreEmpleado varchar(50) not null,
    apellidoCliente varchar(50) not null,
    sueldo decimal(10,2) not null,
    direccionEmpleado varchar(150) not null,
    turno varchar(15) not null,
    CargoEmpleado_codigoCargoEmpleado int not null,
    primary key PK_codigoEmpleado(codigoEmpleado),
    constraint FK_Empleados_CargoEmpleado foreign key Empleados(CargoEmpleado_codigoCargoEmpleado)
		references CargoEmpleado(codigoCargoEmpleado)
);

create table Proveedores(
	codigoProveedor int not null,
    NITProveedor varchar(12) not null,
    nombreProveedor varchar(50)not null,
    apellidoProveedor varchar(50)not null,
    direccionProveedor varchar(150),
    razonSocial varchar(60),
    contactoPrincipal varchar(100),
    paginaWebProveedor varchar(50),
    primary key PK_Proveedores(codigoProveedor)
);

create table TipoProducto(
	codigoTipoProducto int not null,
    descripcion varchar(45),
    primary key PK_TipoProducto(codigoTipoProducto)
);

create table Compras(
	numeroDocumento int not null auto_increment,
    fechaDocumento DATE,
    descripcion varchar(50),
    totalDocumento decimal(10,2),
    primary key PK_Compras(numeroDocumento)
);

create table TelefonoProveedor (
	codigoTelefonoProveedor int not null auto_increment,
    numeroPrincipal varchar(8),
    numeroSecundario varchar(8),
    observaciones varchar(45),
    proveedores_codigoProveedores int not null,
    primary key PK_TelefonoProveedor(codigoTelefonoProveedor),
    foreign key (proveedores_codigoProveedores) references Proveedores(codigoProveedor)
);

create table EmailProveedor (
	codigoEmailProveedor int not null auto_increment,
    emailProveedor varchar(50),
    descripcion varchar(100),
    proveedores_codigoProveedores int not null,
    primary key PK_EmailProveedor(codigoEmailProveedor),
    foreign key (proveedores_codigoProveedores) references Proveedores(codigoProveedor)
);

create table Factura(
	numeroFactura int not null auto_increment,
    estado varchar(50),
    totalFactura decimal(10,2),
    fechaFactura DATE,
    primary key PK_Factura(numeroFactura)
);

create table Productos(
	codigoProducto varchar(15),
    descripcionProducto varchar(45),
    precioUnitario decimal(10,2),
    precioDocena decimal(10,2),
    precioMayor decimal(10,2),
    imagenProducto varchar(45),
    existencia int,
    tipoProducto_codigoTipoProducto int,
    proveedores_codigoProveedor int,
    primary key PK_Productos(codigoProducto),
    foreign key (tipoProducto_codigoTipoProducto) references TipoProducto(codigoTipoProducto),
    foreign key (proveedores_codigoProveedor) references Proveedores(codigoProveedor)
);

create table DetalleCompra(
	codigoDetalleCompra int not null auto_increment,
    costoUnitario decimal(10,2) not null,
    unidad int not null,
    productos_codigoProducto varchar(15),
    compras_numeroDocumento int,
    primary key PK_DetalleCompra(codigoDetalleCompra),
    foreign key (productos_codigoProducto) references Productos(codigoProducto),
    foreign key (compras_numeroDocumento) references Compras(numeroDocumento)
);

create table DetalleFactura(
	codigoDetalleFactura int not null auto_increment,
    precioUnitario decimal(10,2) not null,
    cantidad int not null,
    factura_numeroFactura int not null,
    productos_codigoProducto varchar(15),
    primary key PK_DetalleFactura(codigoDetalleFactura),
    foreign key (factura_numeroFactura) references Factura(numeroFactura),
    foreign key (productos_codigoProducto) references Productos(codigoProducto)
);


-- ------------------------------SP- PROCEDIMIENTOS-------------------------------------------------
-- ------------------------------Agregar Clientes------------------------------------------------------------------
delimiter $$
	create procedure sp_AgregarClientes (in codigoCliente int ,in NITCliente varchar(12),in nombreCliente varchar(50),in apellidoCliente varchar(50),
	in direccionCliente varchar(150) ,in telefonoCliente varchar(12),in correoCliente varchar(45) ) 
		begin
			Insert into Clientes(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
            values(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
	End$$
delimiter ;
call sp_AgregarClientes(1, '123456-7', 'Carmen', 'García', 'zona18, Ciudad A', '123456789', 'Carmen.garcia@example.com');
call sp_AgregarClientes(2, '987654-3', 'Fernando', 'López', 'Avenida Aguilar, Ciudad B', '987654321', 'Fernando.lopez@example.com');
--  -------------------------Listar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_ListarClientes()
    begin
		select C.codigoCliente, 
        C.NITCliente, 
        C.nombreCliente, 
        C.apellidoCliente, 
        C.direccionCliente, 
        C.telefonoCliente, 
        C.correoCliente 
        from Clientes C;
	end $$
delimiter ;
call sp_ListarClientes();
--  -------------------------Buscar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_BuscarClientes(in id int)
    begin
		select C.codigoCliente, 
        C.NITCliente, 
        C.nombreCliente, 
        C.apellidoCliente, 
        C.direccionCliente, 
        C.telefonoCliente, 
        C.correoCliente 
        from Clientes C
        where id = C.codigoCliente;
	end $$
delimiter ;
call sp_BuscarClientes(2);
--  -------------------------Eliminar Clientes-----------------------------------------------
delimiter $$
	create procedure sp_EliminarClientes(in id int)
    begin
		delete from Clientes
        where id = codigoCliente;
	end $$
delimiter ;

--  -------------------------Editar Clientes-----------------------------------------------
delimiter $$
create procedure sp_EditarClientes(in codCliente int ,in nCliente varchar(12),in noCliente varchar(50),in apCliente varchar(50),
	in direcCliente varchar(150) ,in telCliente varchar(12),in corrCliente varchar(45) ) 
		begin
        update Clientes C
        set C.NITCliente=nCliente, 
        C.nombreCliente=noCliente, 
        C.apellidoCliente=apCliente,
        C.direccionCliente=direcCliente, 
        C.telefonoCliente=telCliente, 
        C.correoCliente=corrCliente 
        where C.codigoCliente =codCliente;
	End$$
delimiter ;
-- ------------------------------Proveedores-------------------------------------------------------------------
-- ------------------------------Agregar Proveedores------------------------------------------------------------------
delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int ,in NITProveedor varchar(12),in nombreProveedor varchar(50),in apellidoProveedor varchar(50),
	in direccionProveedor varchar(150) ,in razonSocial varchar(60),in contactoPrincipal varchar(100),in paginaWebProveedor varchar(50) ) 
		begin
			Insert into proveedores(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWebProveedor)
            values(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWebProveedor);
	End$$
delimiter ;
CALL sp_AgregarProveedores(1, '1234567', 'Pedro', 'Martínez', 'Calle Principal 123, Ciudad Guatemala', 'Coca-Cola', '1451528251', 'pedromartinez.com');
CALL sp_AgregarProveedores(2, '7654321', 'María', 'Gómez', 'Avenida Central 456, Ciudad Guatemala', 'Pepsi', '1451528252', 'mariagomez.com');
CALL sp_AgregarProveedores(3, '9876543', 'Luis', 'Hernández', 'Calle Secundaria 789, Ciudad Guatemala', 'Nestlé', '1451528253', 'luishernandez.com');
CALL sp_AgregarProveedores(4, '2345678', 'Ana', 'Pérez', 'Carretera Principal 321, Ciudad Guatemala', 'Kellogg\'s', '1451528254', 'anaperez.com');
CALL sp_AgregarProveedores(5, '8765432', 'Jorge', 'García', 'Boulevard Central 654, Ciudad Guatemala', 'Danone', '1451528255', 'jorgegarcia.com');
SELECT * FROM proveedores

--  -------------------------Listar Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_ListarProveedores()
    begin
		select P.codigoProveedor,
        P.NITProveedor,
        P.nombreProveedor,
        P.apellidoProveedor,
        P.direccionProveedor,
        P.razonSocial,
        P.contactoPrincipal,
        P.paginaWebProveedor
        from proveedores P;
	end $$
delimiter ;
call sp_ListarProveedores();
--  -------------------------Buscar  Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_BuscarProveedores(in id int)
    begin
		select
        P.NITProveedor,
        P.nombreProveedor,
        P.apellidoProveedor,
        P.direccionProveedor,
        P.razonSocial,
        P.contactoPrincipal,
        P.paginaWebProveedor
        from proveedores P
        where id =  P.codigoProveedor;
	end $$
delimiter ;
call sp_BuscarClientes(2);
--  -------------------------Eliminar Proveedores-----------------------------------------------
delimiter $$
	create procedure sp_EliminarProveedor(in id int)
    begin
		delete from Proveedores
        where id = codigoProveedor;
	end $$
delimiter ;

-- call sp_EliminarProveedor(3);
--  -------------------------Editar Proveedores-----------------------------------------------
delimiter $$
create procedure sp_EditarProveedores (in codProveedor int ,in NProveedor varchar(12),in noProveedor varchar(50),in apProveedor varchar(50),
	in direcProveedor varchar(150) ,in rSocial varchar(60),in conPrincipal varchar(100),in pagWebProveedor varchar(50) ) 
		begin
        update proveedores P
        set
        P.NITProveedor=NProveedor,
        P.nombreProveedor=noProveedor,
        P.apellidoProveedor=apProveedor,
        P.direccionProveedor=direcProveedor,
        P.razonSocial=rSocial,
        P.contactoPrincipal=conPrincipal,
        P.paginaWebProveedor=pagWebProveedor
        where P.codigoProveedor =codProveedor;
	End$$
delimiter ;

-- ------------------------------Tipo de Producto-------------------------------------------------------------------
-- ------------------------------Agregar tipo de producto-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int, descripcion varchar(45))
		Begin
			Insert into TipoProducto(codigoTipoProducto, descripcion)
            values (codigoTipoProducto, descripcion);
		End $$
Delimiter ;

CALL sp_AgregarTipoProducto(1, 'Frutas');
CALL sp_AgregarTipoProducto(2, 'Verduras');
CALL sp_AgregarTipoProducto(3, 'Carnes');
CALL sp_AgregarTipoProducto(4, 'Lácteos');
CALL sp_AgregarTipoProducto(5, 'Bebidas');
CALL sp_AgregarTipoProducto(6, 'Panadería');
CALL sp_AgregarTipoProducto(7, 'Congelados');
CALL sp_AgregarTipoProducto(8, 'Cuidado Personal');
CALL sp_AgregarTipoProducto(9, 'Limpieza');
CALL sp_AgregarTipoProducto(10, 'Electrodomésticos');
-- ------------------------------Listar tipo de producto-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarTipoDeProducto()
    Begin
		select
			TP.codigoTipoProducto,
			TP.descripcion
        from TipoProducto TP;
	End $$
Delimiter ;
-- ------------------------------Eliminar tipo de producto-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarTipoDeProducto(in codTP int)
		Begin
			Delete from TipoProducto
            where codigoTipoProducto = codTP;
		End $$
Delimiter ;
-- ------------------------------Editar tipo de producto-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarTipoDeProducto(in codTP int, descTP varchar(45))
		Begin
			Update TipoProducto TP
				set
			TP.codigoTipoProducto = codTP,
			TP.descripcion = descTP
            where codigoTipoProducto = codTP;
		End $$
Delimiter ;
-- ------------------------------Cargo empleado-------------------------------------------------------------------
-- ------------------------------Agregar cargo empleado-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarCargoEmpleado(in codigoCargoEmpleado int, nombreCargo varchar(45), descripcionCargo varchar(45))
		Begin
			insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
            values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
		End $$
Delimiter ;

call sp_AgregarCargoEmpleado(1,'dm','dmorente');
-- ------------------------------Listar cargo empleado-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarCargoEmpleado()
    Begin
		select
			CE.codigoCargoEmpleado,
			CE.nombreCargo,
            CE.descripcionCargo
        from CargoEmpleado CE;
	End $$
Delimiter ;

call sp_ListarCargoEmpleado();
-- ------------------------------Eliminar cargo empleado-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarCargoEmpleado(in codCE int)
		Begin
			Delete from CargoEmpleado
            where codigoCargoEmpleado = codCE;
		End $$
Delimiter ;
-- ------------------------------Editar cargo empleado-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCargoEmpleado(in codCE int, nomCE varchar(45), desCE varchar(45))
		Begin
			Update CargoEmpleado CE
				set
			CE.codigoCargoEmpleado = codCE,
			CE.descripcionCargo = nomCE
            where codigoCargoEmpleado = codCE;
		End $$
Delimiter ;
-- ------------------------------Compras-------------------------------------------------------------------
-- ------------------------------Agregar compras-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarCompras(fechaDocumento date, descripcion varchar(45), totalDocumento decimal(10,2))
		Begin
			insert into Compras(fechaDocumento, descripcion, totalDocumento)
            values (fechaDocumento, descripcion, totalDocumento);
		End $$
Delimiter ;

CALL sp_AgregarCompras('2024-05-01', 'Compra de frutas y verduras', 120.50);
CALL sp_AgregarCompras('2024-05-02', 'Compra de productos lácteos', 85.25);
CALL sp_AgregarCompras('2024-05-03', 'Compra de carne y aves', 220.75);
CALL sp_AgregarCompras('2024-05-05', 'Compra de productos enlatados', 150.80);
CALL sp_AgregarCompras('2024-05-08', 'Compra de productos de limpieza', 75.90);
CALL sp_AgregarCompras('2024-05-10', 'Compra de productos de higiene personal', 90.00);
CALL sp_AgregarCompras('2024-05-12', 'Compra de panadería y repostería', 65.60);
CALL sp_AgregarCompras('2024-05-15', 'Compra de bebidas', 110.45);
CALL sp_AgregarCompras('2024-05-18', 'Compra de alimentos congelados', 180.90);
CALL sp_AgregarCompras('2024-05-20', 'Compra de snacks y dulces', 95.30);

-- ------------------------------Listar compras-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarCompras()
    Begin
		select
			CM.numeroDocumento,
			CM.fechaDocumento,
			CM.descripcion,
            CM.totalDocumento
        from Compras CM;
	End $$
Delimiter ;

call sp_ListarCompras();
-- ------------------------------Eliminar compras-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarCompras(in fecCM int)
		Begin
			Delete from Compras
            where fechaDocumento = fecCM;
		End $$
Delimiter ;
-- ------------------------------Editar compras-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarCompras(numCM int, fecCM date, desCM varchar(50), totCM decimal (10,2))
		Begin
			Update Compras CM
				set
			CM.fechaDocumento = fecCM,
			CM.descripcion = desCM,
            CM.totalDocumento = totCM
            where numeroDocumento = numCM;
		End $$
Delimiter ;
-- ------------------------------Productos-------------------------------------------------------------------
-- ------------------------------Agregar productos-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarProductos(in codigoProducto varchar(15), descripcionProducto varchar(45), precioUnitario decimal(10,2), precioDocena decimal(10,2),
    precioMayor decimal(10,2), imagenProducto varchar(45), existencia int, tipoProducto_codigoTipoProducto int, proveedores_codigoProveedor int)
		Begin
			insert into Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, imagenProducto, existencia, tipoProducto_codigoTipoProducto, proveedores_codigoProveedor)
            values (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, imagenProducto, existencia, tipoProducto_codigoTipoProducto, proveedores_codigoProveedor);
		End $$
Delimiter ;

CALL sp_AgregarProductos('PROD001', 'Manzanas', 2.99, 29.90, 56.50, 'manzanas.jpg', 100, 1, 1);
CALL sp_AgregarProductos('PROD002', 'Plátanos', 1.99, 19.90, 45.50, 'platanos.jpg', 150, 1, 2);
CALL sp_AgregarProductos('PROD003', 'Tomates', 3.49, 34.90, 64.50, 'tomates.jpg', 120, 2, 3);
CALL sp_AgregarProductos('PROD004', 'Lechuga', 1.79, 17.90, 39.50, 'lechuga.jpg', 80, 2, 4);
CALL sp_AgregarProductos('PROD005', 'Filete de res', 8.99, 89.90, 180.50, 'filete_res.jpg', 50, 3, 5);
CALL sp_AgregarProductos('PROD006', 'Pechuga de pollo', 6.49, 64.90, 130.50, 'pechuga_pollo.jpg', 70, 3, 1);
CALL sp_AgregarProductos('PROD007', 'Leche entera', 2.29, 22.90, 45.50, 'leche.jpg', 90, 4, 2);
CALL sp_AgregarProductos('PROD008', 'Yogur natural', 1.59, 15.90, 32.50, 'yogur.jpg', 110, 4, 3);
CALL sp_AgregarProductos('PROD009', 'Agua mineral', 0.99, 9.90, 22.50, 'agua.jpg', 200, 5, 4);
CALL sp_AgregarProductos('PROD010', 'Refresco de cola', 1.89, 18.90, 39.50, 'refresco_cola.jpg', 120, 5, 5);
select * FROM TipoProducto;
select * FROM Proveedores;
select * FROM productos;
-- ------------------------------Listar productos-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarProductos()
    Begin
		select
			PC.codigoProducto,
			PC.descripcionProducto,
            PC.precioUnitario,
            PC.precioDocena,
            PC.precioMayor,
            PC.imagenProducto,
            PC.existencia,
            PC.tipoProducto_codigoTipoProducto as tipoProducto,
            PC.proveedores_codigoProveedor as proveedor
        from Productos PC;
	End $$
Delimiter ;
call sp_ListarProductos();
-- ------------------------------Eliminar productos-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarProductos(in codPC varchar(15))
		Begin
			Delete from Productos
            where codigoProducto = codPC;
		End $$
Delimiter ;

call sp_EliminarProductos(10);
-- ------------------------------Editar productos-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarProductos(codPC varchar(15), desPC varchar(45), punitarioPC decimal (10,2), pdocenaPC decimal (10,2), pmayorPC decimal (10,2), imaPC varchar(45), exisPC int, tipoPC int, proveedoresPC int)
		Begin
			Update Productos PC
				set
				PC.descripcionProducto = desPC,
				PC.precioUnitario = punitarioPC,
				PC.precioDocena = pdocenaPC,
				PC.precioMayor = pmayorPC,
				PC.imagenProducto = imaPC,
				PC.existencia = exisPC,
				PC.tipoProducto_codigoTipoProducto = tipoPC,
				PC.proveedores_codigoProveedor = proveedoresPC
            where codigoProducto = codPC;
		End $$
Delimiter ;
-- ------------------------------Detalle compra-------------------------------------------------------------------
-- ------------------------------Agregar detalle compra-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarDetalleCompra(costoUnitario decimal(10,2), unidad int, codigoProducto varchar(15), numeroDocumento int)
		Begin
			insert into DetalleCompra(costoUnitario, unidad, productos_codigoProducto, compras_numeroDocumento)
            values(costoUnitario, unidad, codigoProducto, numeroDocumento);
		End $$
Delimiter ;

call sp_AgregarDetalleCompra (15.99, 3, 'PROD001', 1);
call sp_AgregarDetalleCompra (10.50, 2, 'PROD002', 2);
call sp_AgregarDetalleCompra (5.75, 4, 'PROD003', 3);
call sp_AgregarDetalleCompra (8.25, 1, 'PROD004', 4);
call sp_AgregarDetalleCompra (5.00, 3, 'PROD005', 5);
call sp_AgregarDetalleCompra (15.00, 4, 'PROD006', 6);
-- ------------------------------Listar detalle compra-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarDetalleCompra()
    Begin
		select
			DC.codigoDetalleCompra,
			DC.costoUnitario,
            DC.unidad,
            DC.productos_codigoProducto as codigoProducto,
            DC.compras_numeroDocumento as numeroDocumento
        from DetalleCompra DC;
	End $$
Delimiter ;
call sp_ListarDetalleCompra();
-- ------------------------------Eliminar detalle compra-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarDetalleCompra(in codDC int)
		Begin
			Delete from DetalleCompra
            where codigoDetalleCompra = codDC;
		End $$
Delimiter ;
-- call sp_EliminarDetalleCompra(6);
-- ------------------------------Editar detalle compra-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarDetalleCompra(detalleDC int,costDC decimal(10,2), uniDC int, codDC varchar(15), numDC int)
		Begin
			Update DetalleCompra DC
				set
				DC.costoUnitario = costDC,
				DC.unidad = uniDC,
				DC.productos_codigoProducto = codDC,
				DC.compras_numeroDocumento = numDC
            where codigoDetalleCompra = detalleDC;
		End $$
Delimiter ;
-- ------------------------------Factura-------------------------------------------------------------------
-- ------------------------------Agregar factura-------------------------------------------------------------------
Delimiter $$
	create procedure sp_AgregarFactura(estado varchar(50), totalFactura decimal(10,2), fechaFactura DATE)
		Begin
			insert into Factura(estado, totalFactura, fechaFactura)
            values (estado, totalFactura, fechaFactura);
		End $$
Delimiter ;

CALL sp_AgregarFactura('Aprobada', 150.75, '2024-05-01');
CALL sp_AgregarFactura('Pendiente', 98.50, '2024-05-02');
CALL sp_AgregarFactura('Aprobada', 215.30, '2024-05-03');
CALL sp_AgregarFactura('Aprobada', 75.20, '2024-05-04');
CALL sp_AgregarFactura('Pendiente', 120.00, '2024-05-05');
CALL sp_AgregarFactura('Aprobada', 180.90, '2024-05-06');
CALL sp_AgregarFactura('Aprobada', 88.25, '2024-05-07');
CALL sp_AgregarFactura('Pendiente', 55.60, '2024-05-08');
CALL sp_AgregarFactura('Aprobada', 132.40, '2024-05-09');
CALL sp_AgregarFactura('Pendiente', 70.80, '2024-05-10');
-- ------------------------------Listar factura-------------------------------------------------------------------
Delimiter $$
	create procedure sp_ListarFactura()
    Begin
		select
			FC.numeroFactura,
			FC.estado,
			FC.totalFactura,
            FC.fechaFactura
        from Factura FC;
	End $$
Delimiter ;

call sp_ListarCompras();
-- ------------------------------Eliminar factura-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarFacturas(in numFC int)
		Begin
			Delete from Factura
            where numeroFactura = numFC;
		End $$
Delimiter ;
-- ------------------------------Editar factura-------------------------------------------------------------------
Delimiter $$
	create procedure sp_EditarFactura(numFC int, estFC varchar(50), totFC decimal(10,2), fecFC DATE)
		Begin
			Update Factura FC
				set
			FC.estado = estFC,
			FC.totalFactura = totFC,
            FC.fechaFactura = fecFC
            where numeroFactura = numFC;
		End $$
Delimiter ;


set time_zone = '-6:00';
