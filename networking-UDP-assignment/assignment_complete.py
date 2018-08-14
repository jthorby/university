import random
import os.path
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
        if not (isinstance(port_number, int) and (1024 < port_number < 64000)):
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



class Channel:
    def __init__(self, channel_s_in, channel_s_out, channel_r_in, channel_r_out, sender_in, receiver_in, prob):
        self.IP = '127.0.0.1'

        self.channel_s_in = int(channel_s_in)
        self.channel_s_out = int(channel_s_out)
        self.channel_r_in = int(channel_r_in)
        self.channel_r_out = int(channel_r_out)
        self.sender_in = int(sender_in)
        self.receiver_in = int(receiver_in)
        self.prob = float(prob)

        self.check_port_numbers(self.channel_s_in, self.channel_s_out, self.channel_r_in,
                                self.channel_r_out, self.receiver_in, self.sender_in)
        self.check_prob(self.prob)

        self.socket_s_in = self.create_socket(self.channel_s_in)
        self.socket_s_out = self.create_socket(self.channel_s_out)
        self.socket_s_out.connect((self.IP, self.sender_in))
        self.socket_r_in = self.create_socket(self.channel_r_in)
        self.socket_r_out = self.create_socket(self.channel_r_out)
        self.socket_r_out.connect((self.IP, self.receiver_in))

        self.data_len_in = None

    def exit(self):
        self.socket_s_in.close()
        self.socket_s_out.close()
        self.socket_r_in.close()
        self.socket_r_out.close()
        exit()

    def check_port_number(self, port_number):
        if not (isinstance(port_number, int) and (1024 < port_number < 64000)):
            print("Port number", port_number, "is not valid. Please provide a vaild port number")
            exit()

    def check_port_numbers(self, channel_s_in, channel_s_out, channel_r_in,
                           channel_r_out, receiver_in, sender_in):
        self.check_port_number(channel_s_in)
        self.check_port_number(channel_s_out)
        self.check_port_number(channel_r_in)
        self.check_port_number(channel_r_out)
        self.check_port_number(receiver_in)
        self.check_port_number(sender_in)

    def check_prob(self, prob):
        if not (0 <= prob < 1):
            print("Probability not valid.")
            exit()

    def create_socket(self, port_number):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

        try:
            sock.bind((self.IP, port_number))
        except OSError:
            print("Port {} is already in use".format(port_number))
            exit()

        return sock

    def run(self):

        while True:
            read_in, _, _ = select.select([self.socket_s_in, self.socket_r_in], [], [])

            if self.socket_s_in in read_in:
                self.incoming_packet(self.socket_s_in, self.socket_r_out, "sender")

            if self.socket_r_in in read_in:
                self.incoming_packet(self.socket_r_in, self.socket_s_out, "receiver")

    def incoming_packet(self, socket_in, socket_out, receiving_socket):
        packet = socket_in.recv(1024)
        header = packet[:16]
        magic_no_in, _, _, self.data_len_in = struct.unpack('iiii', header)

        if not magic_no_in == 0x497E:
            return

        u = random.random()  # need a seed?

        if u < self.prob:
            return
        try:
            socket_out.send(packet)
        except ConnectionRefusedError:
            print("The connection to the {} is no longer available: closing channel"
                  .format(receiving_socket))
            self.exit()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("channel_s_in_port")
    parser.add_argument("channel_s_out_port")
    parser.add_argument("channel_r_in_port")
    parser.add_argument("channel_r_out_port")
    parser.add_argument("sender_in_port")
    parser.add_argument("receiver_in_port")
    parser.add_argument("probability")

    args = parser.parse_args()

    Channel = Channel(args.channel_s_in_port, args.channel_s_out_port,
                      args.channel_r_in_port, args.channel_r_out_port,
                      args.sender_in_port, args.receiver_in_port, args.probability)
    Channel.run()



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
        if not (isinstance(port_number, int) and (1024 < port_number < 64000)):
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