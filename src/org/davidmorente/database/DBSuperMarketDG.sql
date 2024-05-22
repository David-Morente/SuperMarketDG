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
	numeroDocumento int not null,
    fechaDocumento DATE,
    descripcion varchar(50),
    totalDocumento decimal(10,2),
    primary key PK_Compras(numeroDocumento)
);

create table CargoEmpleado(
	codigoCargoEmpleado int,
    nombreCargo varchar(45),
    descripcionCargo varchar(45),
    primary key PK_CargoEmpleado(codigoCargoEmpleado)
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
			where codigoCliente = id;
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
call sp_AgregarProveedores(2, '1234567', 'Juan', 'Lopez', 'Zona 14 , Ciudad Guatemala', 'Pepsi', '1451528252', 'juanlopezconstructor.com');

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
        where codigoProveedor = id;
	end $$
delimiter ;

call sp_EliminarProveedor(3);
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
call sp_AgregarTipoProducto (1, "Leche deslactosada");
call sp_AgregarTipoProducto (2, "Cereal");
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
call sp_ListarTipoDeProducto;
-- ------------------------------Eliminar tipo de producto-------------------------------------------------------------------
Delimiter $$
	Create procedure sp_EliminarTipoDeProducto(in codTP int)
		Begin
			Delete from TipoProducto
            where codigoTipoProducto = codTP;
		End $$
Delimiter ;
call sp_EliminarTipoDeProducto(1);
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

set time_zone = '-6:00';