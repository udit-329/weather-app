import googlemaps
import pprint
import time

APIkey = "AIzaSyDdi82XEr9QPfi4urQVoMyIclG9xkp5AfY"

#client
gmaps = googlemaps.Client(key=APIkey)

#define search
places_result = gmaps.places_nearby(location = '43.698680, -79.418401', radius = 2000, open_now = False, type = 'park')

print(places_result['results'][0]['name'])
print(places_result['results'][1]['name'])
print(places_result['results'][2]['name'])

place_id1 = places_result['results'][0]['place_id']
place_id2 = places_result['results'][0]['place_id']
place_id3 = places_result['results'][0]['place_id']

#define fields

my_fields = ['name', 'formatted_phone_number', 'type']

place_details1 = gmaps.place(place_id = place_id1, fields = my_fields)
place_details2 = gmaps.place(place_id = place_id2, fields = my_fields)
place_details3 = gmaps.place(place_id = place_id3, fields = my_fields)

print(place_details1)
print(place_details2)
print(place_details3)