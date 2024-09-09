namespace DB.Models
{
    public class Question
    {
        public int ID { get; set; }

        public int Nhom {  get; set; }
        public string NoiDung { get; set; }
        public string DapAnDung { get; set; }
        public string DapAnSai1 { get; set; }
        public string DapAnSai2 { get; set; }
        public string DapAnSai3 { get; set; }
    }
}