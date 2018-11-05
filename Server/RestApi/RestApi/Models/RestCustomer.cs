using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RestApi.Models
{
    public class RestCustomer
    {
        public long Id { get; set; }
        public byte[] userdata { get; set; }
        public string RSA { get; set; }
    }
}
