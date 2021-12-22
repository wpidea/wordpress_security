from extract_target import extract_target
from extract_ip import extract_ip

class Visitor():
    def __init__(self, record):
        self.ip = extract_ip(record)
        self.url = extract_target(record)
        self.counter = 0



    
