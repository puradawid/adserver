INSERT INTO users
    (email, first_name, second_name, telephone, password, credencials) 
VALUES
    ("puradawid@gmail.com", "Dawid", "Pura", "795638387", MD5("password"), "ADM"),
    ("client@example.com", "ClientName", "ClientSurname", "5234897589", MD5("password"), "CLI"),
    ("partner@example.com", "PartnerName", "PartnerSurname", "5234897589", MD5("password"), "PAR")
;