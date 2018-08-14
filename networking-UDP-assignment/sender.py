import socket
import argparse
import os.path
import struct
import select


class Sender():
    def __init__(self, sender_in, sender_out, channel_s_in, filename):
        self.IP = '127.0.0.1'
        self.count = 0
        self.magicno = 0x497E
        self.dataPacket = 0
        self.ackPacket = 1

        self.sender_in = int(sender_in)
        self.sender_out = int(sender_out)
        self.channel_s_in = int(channel_s_in)
        self.filename = filename

        self.check_port_numbers(self.sender_in, self.sender_out, self.channel_s_in)

        self.socket_in = self.create_socket(self.sender_in)
        self.socket_out = self.create_socket(self.sender_out)

        self.socket_out.connect((self.IP, self.channel_s_in))

        self.check_file(self.filename)


    def check_port_number(self, port_number):
        if not (isinstance(port_number, int) and (1024 < port_number < 64000) and (type(port_number) == type(1))):
            print("Port", port_number, "is not valid. Please provide a valid port number")
            exit()


    def check_port_numbers(self, sender_in, sender_out, channel_s_in):
        self.check_port_number(sender_in)
        self.check_port_number(sender_out)
        self.check_port_number(channel_s_in)


    def create_socket(self, port_number):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

        try:
            sock.bind((self.IP, port_number))
        except OSError:
            print("Port {} is already in use".format(port_number))
            exit()

        return sock


    def check_file(self, file):
        if not os.path.isfile(file):
            self.exit()

    def exit(self):
        self.socket_in.close()
        self.socket_out.close()
        exit()


    def run(self):

        file = open(self.filename, 'rb')
        exit_flag = False
        next_toggle = 0
        data_out = file.read(512)
        data_len_out = len(data_out)
        packet_buffer = b''

        while exit_flag is False:
            packet_out = struct.pack('iiii', self.magicno, self.dataPacket, next_toggle, data_len_out)

            if data_len_out == 0:
                packet_buffer += packet_out
                exit_flag = True
            elif data_len_out > 0:
                packet_out += data_out
                packet_buffer += packet_out

            going = True

            while going:
                try:
                    self.socket_out.send(packet_buffer)
                except ConnectionRefusedError:
                    print("The connection to the channel is no longer available: closing sender")
                    self.exit()

                self.count += 1
                print("Packet sent, number: ", self.count)
                readable, _, _ = select.select([self.socket_in], [], [], 1)

                if readable:
                    received = self.socket_in.recv(1024) # does not need to be this large
                    magic_no_in, type_in, seq_no_in, data_len_in = struct.unpack('iiii', received)

                    if (type_in != 1) or (data_len_in != 0) or \
                       (seq_no_in != next_toggle) or (magic_no_in != self.magicno):
                        continue
                    elif seq_no_in == next_toggle:
                        next_toggle = 1 - next_toggle

                        if exit_flag is True:
                            file.close()
                            self.exit()
                        else:
                            going = False

            data_out = file.read(512)
            data_len_out = len(data_out)
            packet_buffer = b''


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("sender_in_port")
    parser.add_argument("sender_out_port")
    parser.add_argument("channel_s_in_port")
    parser.add_argument("filename")

    args = parser.parse_args()

    Sender = Sender(args.sender_in_port, args.sender_out_port, args.channel_s_in_port, args.filename)
    Sender.run()



