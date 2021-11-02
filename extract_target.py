
def extract_target(record):
    target = record.split('"')[1]
    target = target.split('/')[1]
    target = target.split('?')[0]
    target = target.split(' HTTP')[0]
    return target

