using Android.App;
using Android.OS;
using Android.Support.V7.App;
using Android.Runtime;
using Android.Widget;
using Com.Huawei.Hms.Maps;
using Android.Util;
using Android;
using Com.Huawei.Agconnect.Config;
using Android.Support.V4.Content;
using Android.Content.PM;
using Android.Support.V4.App;
using Com.Huawei.Hms.Maps.Model;
using Com.Huawei.Hms.Maps.Util;
using Android.Graphics;

namespace XamarinMapDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity, IOnMapReadyCallback
    {


   
        private HuaweiMap hMap;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            checkPermission(new string[] {
                Android.Manifest.Permission.WriteExternalStorage,
                Android.Manifest.Permission.ReadExternalStorage,
                Android.Manifest.Permission.AccessCoarseLocation,
                Android.Manifest.Permission.AccessFineLocation,
                Android.Manifest.Permission.Internet }, 100);

            AGConnectServicesConfig config = AGConnectServicesConfig.FromContext(ApplicationContext);

        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

           // base.OnRequestPermissionsesult(requestCode, permissions, grantResults);
        }
        public void checkPermission(string[] permissions, int requestCode)
        {
            foreach (string permission in permissions)
            {
                if (ContextCompat.CheckSelfPermission(this, permission) == Permission.Denied)
                {
                    ActivityCompat.RequestPermissions(this, permissions, requestCode);
                }
            }
            SetUpMap();
        }
        private void SetUpMap()
        {
            MapFragment mapFragment = FragmentManager.FindFragmentById<MapFragment>(Resource.Id.mapfragment_mapfragmentdemo);

            mapFragment.GetMapAsync(this);
        }

        public void OnMapReady(HuaweiMap huaweiMap)
        {
            this.hMap = huaweiMap;

            hMap.UiSettings.ZoomControlsEnabled = true;
            hMap.UiSettings.CompassEnabled = true;
            hMap.UiSettings.MyLocationButtonEnabled = true;

            hMap.MyLocationEnabled = true;
            MarkersDemo(hMap);
        }

        private void MarkersDemo(HuaweiMap hMap)
        {
            hMap.Clear();

            Marker marker1;
            MarkerOptions marker1Options = new MarkerOptions()
                .InvokePosition(new LatLng(41.0083, 28.9784))
                .InvokeTitle("Marker Title #1")
                .InvokeSnippet("Marker Desc #1");
            marker1 = hMap.AddMarker(marker1Options);

            Marker marker2;
            MarkerOptions marker2Options = new MarkerOptions()
                .InvokePosition(new LatLng(41.022231, 29.008118))
                .InvokeTitle("Marker Title #2")
                .InvokeSnippet("Marker Desc #2");
            marker2 = hMap.AddMarker(marker2Options);

            Marker marker3;
            MarkerOptions marker3Options = new MarkerOptions()
                .InvokePosition(new LatLng(41.005784, 28.997364))
                .InvokeTitle("Marker Title #3")
                .InvokeSnippet("Marker Desc #3");
            Bitmap bitmap1 = ResourceBitmapDescriptor.DrawableToBitmap(this, ContextCompat.GetDrawable(this, Resource.Drawable.markerblue));
            marker3Options.InvokeIcon(BitmapDescriptorFactory.FromBitmap(bitmap1));
            marker3 = hMap.AddMarker(marker3Options);

            Marker marker4;
            MarkerOptions marker4Options = new MarkerOptions()
                .InvokePosition(new LatLng(41.028435, 28.988186))
                .InvokeTitle("Marker Title #4")
                .InvokeSnippet("Marker Desc #4")
                .Anchor(0.9F, 0.9F)
                .Draggable(true);
            Bitmap bitmap2 = ResourceBitmapDescriptor.DrawableToBitmap(this, ContextCompat.GetDrawable(this, Resource.Drawable.marker));
            marker4Options.InvokeIcon(BitmapDescriptorFactory.FromBitmap(bitmap2));
            marker4 = hMap.AddMarker(marker4Options);

        }
    }

}