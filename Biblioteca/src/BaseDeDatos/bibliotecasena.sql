-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-03-2024 a las 07:28:22
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bibliotecasena`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `generos_libros`
--

CREATE TABLE `generos_libros` (
  `ID_GENERO_LIBRO` int(11) NOT NULL,
  `GENERO_DESCRIPCION` varchar(100) NOT NULL,
  `ESTADO` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `generos_libros`
--

INSERT INTO `generos_libros` (`ID_GENERO_LIBRO`, `GENERO_DESCRIPCION`, `ESTADO`) VALUES
(1, 'Fantasía', b'1'),
(2, 'Filosofía', b'1'),
(3, 'Novela', b'1'),
(4, 'Erótico', b'1'),
(5, 'Infantil', b'1'),
(6, 'Romántico', b'1'),
(7, 'Terror', b'1'),
(8, 'Suspenso', b'1'),
(9, 'Poesía', b'1'),
(10, 'Aprendizaje', b'1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `ID_LIBRO` int(11) NOT NULL,
  `IDX_GENERO_LIBRO` int(11) NOT NULL,
  `NOMBRE_LIBRO` varchar(100) NOT NULL,
  `ESTADO` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`ID_LIBRO`, `IDX_GENERO_LIBRO`, `NOMBRE_LIBRO`, `ESTADO`) VALUES
(1, 2, 'Así habló Zaratustra', b'1'),
(2, 1, 'La biblia de los caídos - Tomo 0 del gris', b'1'),
(3, 5, 'El principito', b'1'),
(4, 9, 'La divina comedia', b'1'),
(5, 2, 'El cuervo', b'1'),
(6, 10, 'Diccionario de Inglés', b'1'),
(7, 2, 'El ocaso de los ídolos', b'1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `salidas`
--

CREATE TABLE `salidas` (
  `ID_SALIDA` int(11) NOT NULL,
  `IDX_TIPO_SALIDA` int(11) NOT NULL,
  `IDX_LIBRO` int(11) NOT NULL,
  `NOMBRE_CLIENTE` varchar(100) DEFAULT NULL,
  `CORREO_CLIENTE` varchar(100) DEFAULT NULL,
  `FECHA_SALIDA` datetime NOT NULL,
  `COMENTARIO` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `salidas`
--

INSERT INTO `salidas` (`ID_SALIDA`, `IDX_TIPO_SALIDA`, `IDX_LIBRO`, `NOMBRE_CLIENTE`, `CORREO_CLIENTE`, `FECHA_SALIDA`, `COMENTARIO`) VALUES
(1, 2, 7, 'Daniel Moreno', 'morenodaniel747@gmail.com', '2024-03-16 00:53:24', 'Se devuelve el 20 de Marzo'),
(2, 3, 3, 'Esteban Moreno', 'morenodaniel747@gmail.com', '2024-03-16 00:54:19', 'Se regala por evento de día del niño'),
(3, 1, 5, 'Maria Clara', 'mariaclara@mail.com', '2024-03-16 01:06:24', 'Se vende por #20.00USD');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_salida`
--

CREATE TABLE `tipos_salida` (
  `ID_TIPOS_SALIDA` int(11) NOT NULL,
  `NOMBRE_SALIDA` varchar(100) NOT NULL,
  `ESTADO` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipos_salida`
--

INSERT INTO `tipos_salida` (`ID_TIPOS_SALIDA`, `NOMBRE_SALIDA`, `ESTADO`) VALUES
(1, 'Venta', b'1'),
(2, 'Préstamo', b'1'),
(3, 'Obsequio', b'0');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `ID_USUARIO` int(11) NOT NULL,
  `NOMBRE_USUARIO` varchar(100) NOT NULL,
  `USUARIO` varchar(100) NOT NULL,
  `CONTRASENA` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`ID_USUARIO`, `NOMBRE_USUARIO`, `USUARIO`, `CONTRASENA`) VALUES
(1, 'DANIEL', 'DanielM', 'moreno'),
(2, 'sandra', 'sandra', 'sandra');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `generos_libros`
--
ALTER TABLE `generos_libros`
  ADD PRIMARY KEY (`ID_GENERO_LIBRO`);

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`ID_LIBRO`),
  ADD KEY `IDX_GENERO_LIBRO` (`IDX_GENERO_LIBRO`);

--
-- Indices de la tabla `salidas`
--
ALTER TABLE `salidas`
  ADD PRIMARY KEY (`ID_SALIDA`),
  ADD KEY `IDX_TIPO_SALIDA` (`IDX_TIPO_SALIDA`),
  ADD KEY `IDX_LIBRO` (`IDX_LIBRO`);

--
-- Indices de la tabla `tipos_salida`
--
ALTER TABLE `tipos_salida`
  ADD PRIMARY KEY (`ID_TIPOS_SALIDA`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`ID_USUARIO`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `generos_libros`
--
ALTER TABLE `generos_libros`
  MODIFY `ID_GENERO_LIBRO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `libros`
--
ALTER TABLE `libros`
  MODIFY `ID_LIBRO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `salidas`
--
ALTER TABLE `salidas`
  MODIFY `ID_SALIDA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipos_salida`
--
ALTER TABLE `tipos_salida`
  MODIFY `ID_TIPOS_SALIDA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `ID_USUARIO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `libros`
--
ALTER TABLE `libros`
  ADD CONSTRAINT `libros_ibfk_1` FOREIGN KEY (`IDX_GENERO_LIBRO`) REFERENCES `generos_libros` (`ID_GENERO_LIBRO`);

--
-- Filtros para la tabla `salidas`
--
ALTER TABLE `salidas`
  ADD CONSTRAINT `salidas_ibfk_1` FOREIGN KEY (`IDX_TIPO_SALIDA`) REFERENCES `tipos_salida` (`ID_TIPOS_SALIDA`),
  ADD CONSTRAINT `salidas_ibfk_2` FOREIGN KEY (`IDX_LIBRO`) REFERENCES `libros` (`ID_LIBRO`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
