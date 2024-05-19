CREATE DATABASE Risadog
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

USE Risadog;

CREATE TABLE Endereco
(
	id_End INT PRIMARY KEY AUTO_INCREMENT,
	cep_End INT NOT NULL,
    num_End INT NOT NULL,
	rua_End VARCHAR(45) NOT NULL,
	bairro_End VARCHAR(45) NOT NULL,
	cidade_End VARCHAR(45) NOT NULL
)DEFAULT CHARSET utf8;



CREATE TABLE Cliente
(
	id_Cli INT PRIMARY KEY AUTO_INCREMENT,
	nome_Cli VARCHAR(45) NOT NULL,
	cpf_Cli VARCHAR(14) NOT NULL,
	tel_Cli INT NOT NULL,
	cel_Cli INT NOT NULL,
	email_Cli VARCHAR(50) NOT NULL
)DEFAULT CHARSET utf8;

ALTER TABLE Cliente ADD COLUMN id_End INT;
ALTER TABLE Cliente ADD CONSTRAINT fk_End_Cli FOREIGN KEY (id_End) REFERENCES Endereco(id_End);

CREATE TABLE Colaborador
(
	id_Col INT PRIMARY KEY AUTO_INCREMENT,
	nome_Col VARCHAR(45) NOT NULL,
	cpf_Col VARCHAR(14) NOT NULL,
	tel_Col INT NOT NULL,
	cel_Col INT NOT NULL,
	email_Col VARCHAR(50) NOT NULL
)DEFAULT CHARSET utf8;

ALTER TABLE Colaborador ADD COLUMN id_End INT;
ALTER TABLE Colaborador ADD CONSTRAINT fk_End_Col FOREIGN KEY (id_End) REFERENCES Endereco(id_End);

ALTER TABLE Colaborador
ADD COLUMN user_Col VARCHAR(20);
ALTER TABLE Colaborador
ADD COLUMN user_Senha VARCHAR(20);
ALTER TABLE Colaborador
ADD COLUMN level_Access INT;

CREATE TABLE Fornecedor
(
	id_Forn INT PRIMARY KEY AUTO_INCREMENT,
	nome_Forn VARCHAR(45) NOT NULL,
	cnpj_Forn VARCHAR(18) NOT NULL,
	tel_Forn INT NOT NULL,
	email_Forn VARCHAR(50) NOT NULL
)DEFAULT CHARSET utf8;

ALTER TABLE Fornecedor ADD COLUMN id_End INT;
ALTER TABLE Fornecedor ADD CONSTRAINT fk_End_Forn FOREIGN KEY (id_End) REFERENCES Endereco(id_End);


CREATE TABLE Produto
(
	id_Prod INT PRIMARY KEY AUTO_INCREMENT,
	nome_Prod VARCHAR(45) NOT NULL,
	desc_Prod VARCHAR(45) NOT NULL,
	preco_Forn DECIMAL(20,2) NOT NULL,
	preco_Cli DECIMAL(20,2) NOT NULL,
    qtd_Estocado INT NOT NULL,
    qtd_Min INT NOT NULL
)DEFAULT CHARSET utf8;


CREATE TABLE Local_Estoque
(
	id_Local INT PRIMARY KEY AUTO_INCREMENT,
    nome_Local VARCHAR(45) NOT NULL,
	sit_Local ENUM('A', 'D') NOT NULL,
    desc_Local VARCHAR(70) NOT NULL
)DEFAULT CHARSET utf8;

-- ALTER TABLE Local_Estoque ADD COLUMN id_Prod INT;
-- ALTER TABLE Local_Estoque ADD CONSTRAINT fk_Prod_Local FOREIGN KEY (id_Prod) REFERENCES Produto(id_Prod);


CREATE TABLE Estoque
(
	id_Est INT PRIMARY KEY AUTO_INCREMENT,
    qtd_Prod_Est INT NOT NULL,
    dt_Est Date
)DEFAULT CHARSET utf8;

ALTER TABLE Estoque ADD COLUMN id_Local INT;
ALTER TABLE Estoque ADD CONSTRAINT fk_Local FOREIGN KEY (id_Local) REFERENCES Local_Estoque(id_Local);
ALTER TABLE Estoque ADD COLUMN id_Prod INT;
ALTER TABLE Estoque ADD CONSTRAINT fk_Prod_Est FOREIGN KEY (id_Prod) REFERENCES Produto(id_Prod);



CREATE TABLE Fornecimento
(
	id_Forne INT PRIMARY KEY AUTO_INCREMENT,
	dt_Forne  DATE,
	preco_Forne DECIMAL(20,2) NOT NULL
)DEFAULT CHARSET utf8;

ALTER TABLE Fornecimento ADD COLUMN id_Forn INT;
ALTER TABLE Fornecimento ADD CONSTRAINT fk_Forn FOREIGN KEY (id_Forn) REFERENCES Fornecedor(id_Forn);
ALTER TABLE Fornecimento ADD COLUMN id_Est INT;
ALTER TABLE Fornecimento ADD CONSTRAINT fk_Est FOREIGN KEY (id_Est) REFERENCES Estoque(id_Est);
ALTER TABLE Fornecimento ADD COLUMN id_Prod INT;
ALTER TABLE Fornecimento ADD CONSTRAINT fk_Prod_Forne FOREIGN KEY (id_Prod) REFERENCES Produto(id_Prod);


CREATE TABLE Pedido
(
	id_Ped INT PRIMARY KEY AUTO_INCREMENT,
    dt_Ped DATE,
	preco_Ped DECIMAL(20,2) NOT NULL,
    status_Ped ENUM('AGUARDANDO_PAGAMENTO', 'PAGO', 'CANCELADO') NOT NULL
)DEFAULT CHARSET utf8;

-- ALTER TABLE Pedido ADD COLUMN id_Cli INT;
-- ALTER TABLE Pedido ADD CONSTRAINT fk_Cli_Ped FOREIGN KEY (id_Cli) REFERENCES Cliente(id_Cli);
-- ALTER TABLE Pedido ADD COLUMN id_PedIt INT;
-- ALTER TABLE Pedido ADD CONSTRAINT fk_Ped_itens FOREIGN KEY (id_PedIt) REFERENCES PedidoItems(id_PedIt);
ALTER TABLE Pedido ADD COLUMN id_Col INT;
ALTER TABLE Pedido ADD CONSTRAINT fk_Col FOREIGN KEY (id_Col) REFERENCES Colaborador(id_Col);


CREATE TABLE PedidoItems
(
	id_PedIt INT PRIMARY KEY AUTO_INCREMENT,
    id_Prod INT NOT NULL,
    id_Ped INT NOT NULL,
    qt_PedIt INT NOT NULL,
    preco_PedIt DECIMAL(20,2) NOT NULL
    
);

ALTER TABLE PedidoItems ADD CONSTRAINT fk_Prod_It FOREIGN KEY (id_Prod) REFERENCES Produto(id_Prod);
ALTER TABLE PedidoItems ADD CONSTRAINT fk_Ped_It FOREIGN KEY (id_Ped) REFERENCES Pedido(id_Ped);



CREATE TABLE Nota_Estoque
(
	id_Nota INT PRIMARY KEY AUTO_INCREMENT,
    valor DECIMAL(20,2) NOT NULL,
    nro_Nota INT NOT NULL
)DEFAULT CHARSET utf8;

ALTER TABLE Nota_Estoque ADD COLUMN id_Forne INT;
ALTER TABLE Nota_Estoque ADD CONSTRAINT fk_Forne FOREIGN KEY (id_Forne) REFERENCES Fornecimento(id_Forne);


CREATE TABLE Pagamento
(
	id_Pag INT PRIMARY KEY AUTO_INCREMENT,
	preco_Pag DECIMAL(20,2) NOT NULL,
    dt_Pag DATE,
    tipo_Pag ENUM ('DEBITO', 'CREDITO', 'PIX') NOT NULL,
    nro_Ped INT
)DEFAULT CHARSET utf8;


ALTER TABLE Pedido ADD COLUMN id_Pag INT;
ALTER TABLE Pedido ADD CONSTRAINT fk_Pag FOREIGN KEY (id_Pag) REFERENCES Pagamento(id_Pag);


USE Risadog;

-- Insert data into Endereco
INSERT INTO Endereco (cep_End, num_End, rua_End, bairro_End, cidade_End) VALUES 
(12345678, 10, 'Rua A', 'Bairro A', 'Cidade A');


-- Insert data into Colaborador
INSERT INTO Colaborador (nome_Col, cpf_Col, tel_Col, cel_Col, email_Col, id_End, user_Col, user_Senha, level_Access) VALUES 
('adm', '111.111.111-11', 11111111, 911111111, 'colaborador1@example.com', 1, 'adm', '123', 1);



-- drop database Risadog;
