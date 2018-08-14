import socket
import argparse
import os.path
import struct
import select


class Receiver():
    def __init__(self, receiver_in, receiver_out, channel_r_in, filename):
        self.IP = '127.0.0.1'

        self.receiver_in = int(receiver_in)
        self.receiver_out = int(receiver_out)
        self.channel_r_in = int(channel_r_in)
        self.filename = filename

        self.magicno = 0x497E
        self.ackPacket = 1

        self.check_file(self.filename)

        self.check_port_numbers(self.receiver_in, self.receiver_out, self.channel_r_in)

        self.socket_in = self.create_socket(self.receiver_in)
        self.socket_out = self.create_socket(self.receiver_out)
        self.socket_out.connect((self.IP, self.channel_r_in))


    def check_port_number(self, port_number):
        if not (isinstance(port_number, int) and (1024 < port_number < 64000) and (type(port_number) == type(1))):
            print("Port", port_number, "is not valid. Please provide a valid port number")
            exit()


    def check_port_numbers(self, receiver_in, receiver_out, channel_r_in):
        self.check_port_number(receiver_in)
        self.check_port_number(receiver_out)
        self.check_port_number(channel_r_in)


    def create_socket(self, port_number):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        try:
            sock.bind((self.IP, port_number))
        except OSError:
            print("Port {} is already in use".format(port_number))
            exit()
        return sock


    def check_file(self, filename):
        if os.path.isfile(self.filename):
            print("This file already exists. Please give a new file name")
            self.exit()


    def exit(self):
        self.socket_in.close()
        self.socket_out.close()
        exit()

    def run(self):
        expected = 0
        file = open(self.filename, 'ab')
        running = True

        while running:
            read_in, _, _ = select.select([self.socket_in], [], [])
            if read_in:
                received = self.socket_in.recv(1024)
                header = received[:16]
                data_in = received[16:]
                magic_no_in, type_in, seq_no_in, data_len_in = struct.unpack('iiii', header)

                if (magic_no_in != self.magicno) and (type_in != 0):
                    continue

                if seq_no_in != expected:
                    data_len_to_send = 0
                    packet_out = struct.pack('iiii', self.magicno, self.ackPacket, seq_no_in, data_len_to_send)
                    self.socket_out.send(packet_out)
                    continue

                if seq_no_in == expected:
                    data_len_to_send = 0
                    packet_out = struct.pack('iiii', self.magicno, self.ackPacket, seq_no_in, data_len_to_send)

                    try:
                        self.socket_out.send(packet_out)
                    except ConnectionRefusedError:
                        print("The connection to the sender is no longer available: closing receiver")
                        self.exit()

                    expected = 1 - expected

                    if data_len_in > 0:
                        file.write(data_in)
                        continue

                    elif data_len_in == 0:
                        file.close()
                        self.exit()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("receiver_in_port")
    parser.add_argument("receiver_out_port")
    parser.add_argument("channel_r_in_port")
    parser.add_argument("filename")

    args = parser.parse_args()

    Receiver = Receiver(args.receiver_in_port, args.receiver_out_port,
                        args.channel_r_in_port, args.filename)
    Receiver.run()
