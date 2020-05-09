-- Users

INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t1',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet2','v3t2',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet3','v3t3',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet4','v3t4',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet5','v3t5',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet6','v3t6',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('vet7','v3t7',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('trainer1','tr41n3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('trainer2','train333',TRUE);

-- Authorities

INSERT INTO authorities(username, authority) VALUES ('admin1','admin');
INSERT INTO authorities(username, authority) VALUES ('owner1','owner');
INSERT INTO authorities(username, authority) VALUES ('owner1','owner');
INSERT INTO authorities(username, authority) VALUES ('owner2','owner');
INSERT INTO authorities(username, authority) VALUES ('owner3','owner');
INSERT INTO authorities(username, authority) VALUES ('owner4','owner');
INSERT INTO authorities(username, authority) VALUES ('owner5','owner');
INSERT INTO authorities(username, authority) VALUES ('owner6','owner');
INSERT INTO authorities(username, authority) VALUES ('owner7','owner');
INSERT INTO authorities(username, authority) VALUES ('owner8','owner');
INSERT INTO authorities(username, authority) VALUES ('owner9','owner');
INSERT INTO authorities(username, authority) VALUES ('owner10','owner');
INSERT INTO authorities(username, authority) VALUES ('vet1','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet2','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet3','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet4','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet5','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet6','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('vet7','veterinarian');
INSERT INTO authorities(username, authority) VALUES ('trainer1','trainer');
INSERT INTO authorities(username, authority) VALUES ('trainer2','trainer');

-- Vets

INSERT INTO vets(id, first_name, last_name, username) VALUES (1, 'James', 'Carter','vet1');
INSERT INTO vets(id, first_name, last_name, username) VALUES (2, 'Helen', 'Leary','vet2');
INSERT INTO vets(id, first_name, last_name, username) VALUES (3, 'Linda', 'Douglas','vet3');
INSERT INTO vets(id, first_name, last_name, username) VALUES (4, 'Rafael', 'Ortega','vet4');
INSERT INTO vets(id, first_name, last_name, username) VALUES (5, 'Henry', 'Stevens','vet5');
INSERT INTO vets(id, first_name, last_name, username) VALUES (6, 'Sharon', 'Jenkins','vet6');
INSERT INTO vets(id, first_name, last_name, username) VALUES (7, 'Test', 'Dummy','vet7');

-- Specialties

INSERT INTO specialties(id, name) VALUES (1, 'radiology');
INSERT INTO specialties(id, name) VALUES (2, 'surgery');
INSERT INTO specialties(id, name) VALUES (3, 'dentistry');

-- Vets' specialties

INSERT INTO vet_specialties(vet_id, specialty_id) VALUES (2, 1);
INSERT INTO vet_specialties(vet_id, specialty_id) VALUES (3, 2);
INSERT INTO vet_specialties(vet_id, specialty_id) VALUES (3, 3);
INSERT INTO vet_specialties(vet_id, specialty_id) VALUES (4, 2);
INSERT INTO vet_specialties(vet_id, specialty_id) VALUES (5, 1);

-- Types

INSERT INTO types(id, name) VALUES (1, 'cat');
INSERT INTO types(id, name) VALUES (2, 'dog');
INSERT INTO types(id, name) VALUES (3, 'lizard');
INSERT INTO types(id, name) VALUES (4, 'snake');
INSERT INTO types(id, name) VALUES (5, 'bird');
INSERT INTO types(id, name) VALUES (6, 'hamster');

-- Owners

INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners(id, first_name, last_name, address, city, telephone, username) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

-- Pets

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
-- Homeless Pets
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (14, 'Tucker', '2018-06-08', 2, null);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (15, 'Lekay', '2016-04-04', 1, null);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (16, 'Miss', '2015-03-17', 1, null);

-- Visits

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');
-- Homeless Pets' visits
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (5, 14, '2018-07-09', 'Description 1');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (6, 14, '2018-08-09', 'Description 2');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (7, 15, '2017-08-09', 'Description 3');

-- Trainers

INSERT INTO trainers(id, first_name, last_name, email, phone, username) VALUES (1, 'John', 'Doe', 'acme@mail.com', '34 111111111', 'trainer1');
INSERT INTO trainers(id, first_name, last_name, email, phone, username) VALUES (2, 'Thomas', 'Moon', 'thomas@mail.com', '35 824456756', 'trainer2');

-- Interventions

-- Homeless Pets' interventions
INSERT INTO interventions(id,pet_id, intervention_date, intervention_time, intervention_description, vet_id) VALUES (1, 14, '2020-09-09', '4', 'Surgery',3);
INSERT INTO interventions(id,pet_id, intervention_date, intervention_time, intervention_description, vet_id) VALUES (2, 1, '2020-09-09', '4', 'Surgery',1);
INSERT INTO interventions(id,pet_id, intervention_date, intervention_time, intervention_description, vet_id) VALUES (3, 1, '2020-09-09', '4', 'Surgery',2);

-- Rehabs

-- Homeless Pets' rehabs
INSERT INTO rehab(id, pet_id, rehab_date, rehab_time, description, trainer_id) VALUES (1, 14, '2020-09-11', '2', 'Rehab session 1', 1);

-- Medical Records

INSERT INTO medical_records(id, description, status, visit_id) VALUES (1, 'Test', 'TestStatus', 1);
INSERT INTO medical_records(id, description, status, visit_id) VALUES (2, 'Test description', 'Test status', 5);

-- Medicines

INSERT INTO medicine(id, name, expiration_date, maker, type_id) VALUES (1, 'Cat medicine', '2023-05-22', 'Maker', 1);
INSERT INTO medicine(id, name, expiration_date, maker, type_id) VALUES (2, 'Dog medicine', '2023-05-22', 'Maker', 2);

-- Prescriptions

INSERT INTO prescription(id, medicine_id, medical_record_id, dose) VALUES (1, 1, 1, 'Every 24 hours');
INSERT INTO prescription(id, medicine_id, medical_record_id, dose) VALUES (2, 2, 2, 'Every 4 hours');

-- Adoptions

INSERT INTO adoption(id, date, pet_id, owner_id) VALUES (1, '2020-03-03', 1, 1);