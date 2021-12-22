import re

def extract_ip(record):
    pattern = re.compile(r'(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})')
    ip = pattern.search(record)[0]
    return ip
