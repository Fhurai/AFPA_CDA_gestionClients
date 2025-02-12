-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 11 fév. 2025 à 08:02
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionclients`
--

-- --------------------------------------------------------

--
-- Structure de la table `adresses`
--

DROP TABLE IF EXISTS `adresses`;
CREATE TABLE IF NOT EXISTS `adresses` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `numRue` varchar(10) NOT NULL,
  `nomRue` varchar(30) NOT NULL,
  `codePostal` varchar(5) NOT NULL,
  `ville` varchar(30) NOT NULL,
  PRIMARY KEY (`identifiant`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `adresses`
--

INSERT INTO `adresses` (`identifiant`, `numRue`, `nomRue`, `codePostal`, `ville`) VALUES
(1, '2bis', 'rue Ardant du Picq', '57004', 'Metz'),
(2, '25', 'rue de la Taye', '57130', 'Jussy'),
(3, '3', 'rue des Michottes', '54000', 'Nancy'),
(4, '28', 'Boulevard Albert 1er', '54000', 'NANCY'),
(5, '80', 'QUAI VOLTAIRE', '95870', 'BEZONS'),
(6, '276', 'AVENUE DU PRESIDENT WILSON', '93210', 'SAINT-DENIS'),
(7, '25', 'rue Serpenoise', '57000', 'Metz');

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `raisonSociale` varchar(30) NOT NULL,
  `telephone` varchar(15) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `commentaires` text,
  `chiffreAffaires` decimal(19,4) NOT NULL,
  `nbEmployes` int NOT NULL,
  `idAdresse` int NOT NULL,
  PRIMARY KEY (`identifiant`),
  UNIQUE KEY `raisonSociale` (`raisonSociale`),
  KEY `idAdresse` (`idAdresse`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`identifiant`, `raisonSociale`, `telephone`, `mail`, `commentaires`, `chiffreAffaires`, `nbEmployes`, `idAdresse`) VALUES
(1, 'Efluid SAS', '0387543400', 'contact@efluid.fr', NULL, 999999999999999.9999, 250, 1),
(2, 'OGMI', '0383375640', 'ogmi@ogmi.fr', NULL, 50000.0000, 1, 3),
(3, 'Happiso', '0387758575', 'contact@happiso.fr', NULL, 4813.0000, 6, 2);

-- --------------------------------------------------------

--
-- Structure de la table `contrats`
--

DROP TABLE IF EXISTS `contrats`;
CREATE TABLE IF NOT EXISTS `contrats` (
  `idContrat` int NOT NULL AUTO_INCREMENT,
  `libelleContrat` varchar(30) NOT NULL,
  `montant` decimal(19,4) NOT NULL,
  `idClient` int NOT NULL,
  PRIMARY KEY (`idContrat`),
  KEY `idClient` (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `contrats`
--

INSERT INTO `contrats` (`idContrat`, `libelleContrat`, `montant`, `idClient`) VALUES
(1, 'Contrat test Efluid', 1500.0000, 1),
(2, 'Contrat Interim Efluid', 45000.0000, 1),
(3, 'Contrat Pro Happiso', 75000.0000, 3);

-- --------------------------------------------------------

--
-- Structure de la table `prospects`
--

DROP TABLE IF EXISTS `prospects`;
CREATE TABLE IF NOT EXISTS `prospects` (
  `identifiant` int NOT NULL AUTO_INCREMENT,
  `raisonSociale` varchar(30) NOT NULL,
  `telephone` varchar(15) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `commentaires` text,
  `dateProspection` date NOT NULL,
  `prospectInteresse` tinyint(1) NOT NULL,
  `idAdresse` int NOT NULL,
  PRIMARY KEY (`identifiant`),
  UNIQUE KEY `raisonSociale` (`raisonSociale`),
  KEY `idAdresse` (`idAdresse`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `prospects`
--

INSERT INTO `prospects` (`identifiant`, `raisonSociale`, `telephone`, `mail`, `commentaires`, `dateProspection`, `prospectInteresse`, `idAdresse`) VALUES
(1, 'CGI', '0388553370', 'contact@cgi.com', NULL, '2025-10-30', 0, 4),
(2, 'ATOS', '0173260000', 'contact@atos.fr', NULL, '2024-05-28', 0, 5),
(3, 'Expectra', '0387172390', 'contact@expectra.fr', NULL, '2024-10-10', 1, 6),
(4, 'Artistsnclients', '0354626298', 'luku@free.fr', 'test', '2023-10-15', 0, 7);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `clients`
--
ALTER TABLE `clients`
  ADD CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`idAdresse`) REFERENCES `adresses` (`identifiant`);

--
-- Contraintes pour la table `contrats`
--
ALTER TABLE `contrats`
  ADD CONSTRAINT `contrats_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `clients` (`identifiant`);

--
-- Contraintes pour la table `prospects`
--
ALTER TABLE `prospects`
  ADD CONSTRAINT `prospects_ibfk_1` FOREIGN KEY (`idAdresse`) REFERENCES `adresses` (`identifiant`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
