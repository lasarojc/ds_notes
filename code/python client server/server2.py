#server.py
#!/usr/bin/python                               # This is server.py file

import socket                                   # Import socket module

s = socket.socket()                             # Create a socket object
host = "0.0.0.0"
port = 12345                                    # Reserve a port for your service.
s.bind((host, port))                            # Bind to the port

s.listen(5)                                     # Now wait for client connections.
while True:
   (c, addr) = s.accept()                         # Establish connection with client.
   print('Got connection from', addr)
   i = 0
   while True: #Só sairemos do loop quando a conexão for quebrada
       try:
           i = i + 1
           print(i)
           c.send(("teste um pouco mais longo " + str(i)).encode())
       except:
           break

