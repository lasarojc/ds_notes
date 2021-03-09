#client.py

#!/usr/bin/python                               # This is client.py file

import socket                                   # Import socket module

s = socket.socket()                             # Create a socket object
host = socket.gethostname()                # Get local machine name
port = 12345                                    # Reserve a port for your service.

s.connect((host, port))


total = 5000
recebido = 0
msg = ''

while recebido < total:
   data = s.recv(1024)
   recebido += len(data)
   print(len(data))
   msg += data.decode()

print(msg)
s.close()     
