import argparse
import socket
import select
import struct
import random


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
        if not (isinstance(port_number, int) and (1024 < port_number < 64000) and (type(port_number) == type(1))):
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

        u = random.random() # need a seed?

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